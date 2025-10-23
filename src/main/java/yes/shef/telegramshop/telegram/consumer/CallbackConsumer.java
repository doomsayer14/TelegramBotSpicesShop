package yes.shef.telegramshop.telegram.consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import yes.shef.telegramshop.service.ProductService;
import yes.shef.telegramshop.telegram.service.TelegramService;
import yes.shef.telegramshop.util.Commands;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
@RequiredArgsConstructor
public class CallbackConsumer {

    private static final String QTY_DEC = "QTY_DEC";
    private static final String QTY_INC = "QTY_INC";
    private static final String ADD_TO_CART = "ADD_TO_CART";

    private static final int MIN_QUANTITY = 1;
    private static final int MAX_QUANTITY = 99;

    private final TelegramService telegramService;
    private final ProductService productService;

    // chatId -> (productId -> quantity)
    private final Map<Long, Map<Long, Integer>> quantityCache = new ConcurrentHashMap<>();

    public boolean consume(Update update) {
        if (!update.hasCallbackQuery()) {
            return false;
        }

        var cq = update.getCallbackQuery();
        if (cq == null || cq.getMessage() == null || cq.getData() == null) {
            safeAnswer(cq, "Помилка");
            return true;
        }

        String data = cq.getData();

        if ("noop".equals(data)) {
            telegramService.answerCallback(cq.getId(), "");
            return true;
        }

        ParsedCallback parsed = parseCallbackData(data);
        if (parsed == null) {
            safeAnswer(cq, Commands.UNKNOWN_ACTION_MESSAGE);
            return true;
        }

        Long chatId = cq.getMessage().getChatId();
        Integer messageId = cq.getMessage().getMessageId();

        try {
            if (QTY_DEC.equals(parsed.action)) {
                handleQtyDec(cq.getId(), chatId, messageId, parsed.productId);
                return true;
            }
            if (QTY_INC.equals(parsed.action)) {
                handleQtyInc(cq.getId(), chatId, messageId, parsed.productId);
                return true;
            }
            if (ADD_TO_CART.equals(parsed.action)) {
                handleAddToCart(cq.getId(), chatId, parsed.productId);
                return true;
            }
            safeAnswer(cq, Commands.UNKNOWN_ACTION_MESSAGE);
        } catch (Exception ex) {
            log.error("Callback handling error", ex);
            safeAnswer(cq, "Сталася помилка");
        }
        return true;
    }

    public void setQuantity(Long chatId, Long productId, int quantity) {
        Map<Long, Integer> perChat = quantityCache.computeIfAbsent(chatId, k -> new ConcurrentHashMap<>());
        perChat.put(productId, quantity);
    }

    private int getQuantity(Long chatId, Long productId) {
        Map<Long, Integer> perChat = quantityCache.get(chatId);
        if (perChat == null) {
            return 1;
        }
        Integer value = perChat.get(productId);
        if (value == null) {
            return 1;
        }
        return value;
    }

    private void handleQtyDec(String callbackId, Long chatId, Integer messageId, Long productId) {
        int current = getQuantity(chatId, productId);
        int newValue = current - 1;
        if (newValue < MIN_QUANTITY) {
            newValue = MIN_QUANTITY;
        }
        setQuantity(chatId, productId, newValue);
        telegramService.editProductQuantityMarkup(chatId, messageId, productId, newValue);
        telegramService.answerCallback(callbackId, "К-ть: " + newValue);
    }

    private void handleQtyInc(String callbackId, Long chatId, Integer messageId, Long productId) {
        int current = getQuantity(chatId, productId);
        int newValue = current + 1;
        if (newValue > MAX_QUANTITY) {
            newValue = MAX_QUANTITY;
        }
        setQuantity(chatId, productId, newValue);
        telegramService.editProductQuantityMarkup(chatId, messageId, productId, newValue);
        telegramService.answerCallback(callbackId, "К-ть: " + newValue);
    }

    private void handleAddToCart(String callbackId, Long chatId, Long productId) {
        int quantity = getQuantity(chatId, productId);
        String title = productService.getProductNameById(productId);
        telegramService.answerCallback(callbackId, "Додано: " + quantity + "× " + title + " ✅");
        // TODO: интеграция с CartService при готовности
    }

    private ParsedCallback parseCallbackData(String data) {
        String[] parts = data.split(":", 2);
        if (parts.length < 2) {
            return null;
        }
        String action = parts[0];
        Long productId;
        try {
            productId = Long.parseLong(parts[1]);
        } catch (NumberFormatException ex) {
            return null;
        }
        return new ParsedCallback(action, productId);
    }

    private void safeAnswer(org.telegram.telegrambots.meta.api.objects.CallbackQuery cq, String text) {
        if (cq == null) {
            return;
        }
        telegramService.answerCallback(cq.getId(), text);
    }

    private record ParsedCallback(String action, Long productId) {
    }
}
