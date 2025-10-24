package yes.shef.telegramshop.telegram.consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import yes.shef.telegramshop.entity.Customer;
import yes.shef.telegramshop.entity.Order;
import yes.shef.telegramshop.entity.OrderItem;
import yes.shef.telegramshop.entity.Product;
import yes.shef.telegramshop.repository.OrderRepository;
import yes.shef.telegramshop.service.CustomerService;
import yes.shef.telegramshop.service.OrderService;
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

    private static final String CART_CLEAR = "CART_CLEAR";
    private static final String CART_CHECKOUT = "CART_CHECKOUT";

    private static final int MIN_QUANTITY = 1;
    private static final int MAX_QUANTITY = 99;

    private final TelegramService telegramService;
    private final ProductService productService;
    private final OrderService orderService;
    private final CustomerService customerService;
    private final OrderRepository orderRepository;

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
                handleAddToCart(cq, chatId, parsed.productId);
                return true;
            }
            if (CART_CLEAR.equals(parsed.action)) {
                handleCartClear(cq, parsed.entityId);
                return true;
            }
            if (CART_CHECKOUT.equals(parsed.action)) {
                handleCartCheckout(cq, parsed.entityId);
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

    public void handleAddToCart(CallbackQuery cq, Long chatId, Long productId) {
        int quantity = getQuantity(chatId, productId);
        Product product = productService.getProductById(productId);
        Customer customer = customerService.getCustomerByTelegramId(cq.getFrom().getId());

        OrderItem orderItem = OrderItem.builder()
                .product(product)
                .quantity(quantity)
                .price(product.getPrice())
                .build();

        Order order = orderService.getOrCreateUnpaidOrder(customer.getId());
        orderService.addToOrder(order.getId(), orderItem);

        telegramService.answerCallback(cq.getId(),
                "Додано: " + quantity + "× " + product.getName() + " ✅");
    }

    public void handleCartClear(CallbackQuery cq, Long orderId) {
        if (orderId == null) {
            safeAnswer(cq, "Замовлення не знайдено");
            return;
        }

        // Используем твой готовый метод:
        orderService.emptyOrder(orderId);

        telegramService.answerCallback(cq.getId(), "Кошик очищено ✅");
        telegramService.sendMessage(cq.getMessage().getChatId(), "🛒 Ваш кошик порожній.");
    }

    public void handleCartCheckout(CallbackQuery cq, Long orderId) {
        if (orderId == null) {
            safeAnswer(cq, "Замовлення не знайдено");
            return;
        }

        telegramService.answerCallback(cq.getId(), "Починаємо оформлення ✅");
        telegramService.sendMessage(
                cq.getMessage().getChatId(),
                "Будь ласка, вкажіть ваше ПІБ та номер телефону 📞"
        );
    }

    private ParsedCallback parseCallbackData(String data) {
        String[] parts = data.split(":", 2);

        String action = parts[0];
        Long id = null;

        if (parts.length == 2) {
            try {
                id = Long.parseLong(parts[1]);
            } catch (NumberFormatException ignored) {
            }
        }

        // productId и entityId одинаковые (в зависимости от действия мы используем то, что нужно)
        return new ParsedCallback(action, id, id);
    }

    private void safeAnswer(org.telegram.telegrambots.meta.api.objects.CallbackQuery cq, String text) {
        if (cq == null) {
            return;
        }
        telegramService.answerCallback(cq.getId(), text);
    }

    private record ParsedCallback(String action, Long productId, Long entityId) {}
}
