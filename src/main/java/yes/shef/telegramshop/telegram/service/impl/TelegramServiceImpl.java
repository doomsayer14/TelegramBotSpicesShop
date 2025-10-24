package yes.shef.telegramshop.telegram.service.impl;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import yes.shef.telegramshop.dto.CustomerDto;
import yes.shef.telegramshop.entity.Customer;
import yes.shef.telegramshop.entity.Order;
import yes.shef.telegramshop.entity.OrderItem;
import yes.shef.telegramshop.entity.Product;
import yes.shef.telegramshop.service.CustomerService;
import yes.shef.telegramshop.service.OrderService;
import yes.shef.telegramshop.service.ProductService;
import yes.shef.telegramshop.telegram.service.TelegramService;
import yes.shef.telegramshop.util.Commands;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Actually not a class for business logic but for some methods for working directly
 * with telegram.
 */
@Service
@RequiredArgsConstructor
public class TelegramServiceImpl implements TelegramService {

    private static final Logger log = LoggerFactory.getLogger(TelegramServiceImpl.class);
    /**
     * Token from BotFather. Don't forget to clean it from application.properties.
     */
    @Value("${telegram.bot.token}")
    private String token;

    private TelegramClient telegramClient;

    private final ProductService productService;
    private final CustomerService customerService;
    private final OrderService orderService;



    /**
     * Need this to be sure that @Value will be instantiated before it will be used.
     */
    @PostConstruct
    public void init() {
        this.telegramClient = new OkHttpTelegramClient(token);
    }

    @Override
    public void sendProduct(Long chatId, Product product) {
        sendProduct(chatId, product, 1); // количество по умолчанию = 1
    }

    public void sendProduct(Long chatId, Product product, int quantity) {
        String caption = buildCaption(product);
        byte[] bytes = product.getImageBytes();
        InlineKeyboardMarkup inlineKeyboard = buildQuantityInlineKeyboard(product.getId(), quantity);

        if (bytes != null && bytes.length > 0) {
            InputStream is = new ByteArrayInputStream(bytes);

            SendPhoto sendPhoto = SendPhoto.builder()
                    .chatId(chatId)
                    .photo(new InputFile(is, "product.jpg"))
                    .caption(caption)
                    .replyMarkup(inlineKeyboard)
                    .build();

            executeMessage(sendPhoto);
            return;
        }

        SendMessage sendMessage = SendMessage.builder()
                .chatId(chatId)
                .text(caption)
                .replyMarkup(inlineKeyboard)
                .build();

        executeMessage(sendMessage);
    }

    @Override
    public void sendStartMenu(Update update, String message) {
        if (message.equals(Commands.START_MESSAGE)) {
            User user = update.getMessage().getFrom();
            customerService.createCustomer(CustomerDto.builder()
                    .telegramId(update.getMessage().getFrom().getId())
                    .firstName(user.getFirstName())
                    .secondName(user.getLastName())
                    .username(user.getUserName())
                    .build());
        }
        SendMessage sendMessage = SendMessage.builder()
                .text(message)
                .chatId(update.getMessage().getChatId())
                .build();

        List<KeyboardRow> keyboardRowList = List.of(
                new KeyboardRow(Commands.CATALOGUE_COMMAND),
                new KeyboardRow(Commands.MY_CART_COMMAND, Commands.TERMS_OF_USE_COMMAND),
                new KeyboardRow(Commands.ABOUT_ME_COMMAND)
        );

        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup(keyboardRowList);
        replyKeyboardMarkup.setResizeKeyboard(true);
        sendMessage.setReplyMarkup(replyKeyboardMarkup);

        executeMessage(sendMessage);
    }

    public void sendCatalogue(Long chatId) {

        List<Product> productList = productService.getAllProducts();

        List<KeyboardRow> keyboardRowList = new ArrayList<>();
        KeyboardRow keyboardRow = new KeyboardRow();

        // 3 products in row
        int perRow = 3;
        for (int i = 0; i < productList.size(); i++) {
            keyboardRow.add(productList.get(i).getName());
            if ((i + 1) % perRow == 0) {
                keyboardRowList.add(keyboardRow);
                keyboardRow = new KeyboardRow();
            }
        }
        if (!keyboardRow.isEmpty()) {
            keyboardRowList.add(keyboardRow);
        }
        keyboardRowList.add(new KeyboardRow(Commands.GO_TO_MAIN_MENU_COMMAND));


        SendMessage sendMessage = SendMessage.builder()
                .text(Commands.CATALOGUE_HEADER_MESSAGE)
                .chatId(chatId)
                .replyMarkup(new ReplyKeyboardMarkup(keyboardRowList))
                .build();

        executeMessage(sendMessage);
    }

    public void sendCart(Long chatId) {
        Customer customer = customerService.getCustomerByTelegramId(chatId);

        var optionalCart = orderService.getUnpaidOrder(customer.getId());
        if (optionalCart.isEmpty() || optionalCart.get().getOrderItems().isEmpty()) {
            executeMessage(SendMessage.builder()
                    .chatId(chatId)
                    .text("🛒 Ваш кошик порожній.")
                    .build());
            return;
        }

        Order cart = optionalCart.get();
        String text = buildCartText(cart);

        SendMessage msg = SendMessage.builder()
                .chatId(chatId)
                .text(text)
                .parseMode("Markdown")
                .replyMarkup(buildCartActionsKeyboard(cart.getId()))
                .build();

        executeMessage(msg);
    }


    @Override
    public void sendMessage(Long chatId, String message) {
        SendMessage sendMessage = SendMessage.builder()
                .text(message)
                .chatId(chatId)
                .build();

        executeMessage(sendMessage);
    }

    public void editProductQuantityMarkup(Long chatId, Integer messageId, Long productId, int quantity) {
        InlineKeyboardMarkup kb = buildQuantityInlineKeyboard(productId, quantity);

        EditMessageReplyMarkup edit = EditMessageReplyMarkup.builder()
                .chatId(chatId)
                .messageId(messageId)
                .replyMarkup(kb)
                .build();
        try {
            telegramClient.execute(edit);
        } catch (org.telegram.telegrambots.meta.exceptions.TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void answerCallback(String callbackId, String text) {
        AnswerCallbackQuery ans = AnswerCallbackQuery.builder()
                .callbackQueryId(callbackId)
                .text(text)
                .showAlert(false)
                .build();
        try {
            telegramClient.execute(ans);
        } catch (org.telegram.telegrambots.meta.exceptions.TelegramApiException e) {
            e.printStackTrace();
        }
    }


    /**
     * Name, description and price for {@link Product}.
     *
     * @param product {@link Product} which needs to be described.
     * @return full text about {@link Product}.
     */
    private String buildCaption(Product product) {
        StringBuilder sb = new StringBuilder();

        // Название
        if (product.getName() != null) {
            sb.append(product.getName());
        } else {
            sb.append("Товар");
        }
        sb.append(System.lineSeparator()).append(System.lineSeparator());

        // Описание
        if (product.getDescription() != null && !product.getDescription().isBlank()) {
            sb.append(product.getDescription())
                    .append(System.lineSeparator())
                    .append(System.lineSeparator());
        }

        // Цена
        sb.append("Ціна - ");
        if (product.getPrice() != null) {
            sb.append(product.getPrice());
        } else {
            sb.append("—");
        }
        sb.append(" грн.");

        String caption = sb.toString();
        // Telegram ограничивает caption до 1024 символов
        if (caption.length() > 1024) {
            caption = caption.substring(0, 1021) + "...";
        }
        return caption;
    }


    private String buildCartText(Order cart) {
        StringBuilder sb = new StringBuilder("🛒 *Ваш кошик:*\n");
        BigDecimal total = BigDecimal.ZERO;
        int i = 1;

        for (OrderItem item : cart.getOrderItems()) {
            String name = item.getProduct().getName();
            int qty = item.getQuantity();
            BigDecimal price = item.getPrice();                  // цена за 1 шт.
            BigDecimal line = price.multiply(BigDecimal.valueOf(qty));
            total = total.add(line);

            sb.append(i++).append(") ").append(name)
                    .append(" — ").append(qty).append(" шт. × ")
                    .append(price).append(" ₴ = ")
                    .append(line).append(" ₴\n");
        }
        sb.append("--------------------------\n")
                .append("*Разом:* ").append(total).append(" ₴");

        return sb.toString();
    }


    /**
     * Builds menu like
     * ➖ 1 ➕
     * 🛒 Додати
     *
     * @param productId {@link Product} which needs keyboard.
     * @param quantity  quantity of {@link Product} to
     * @return ready inline keyboard.
     */
    private InlineKeyboardMarkup buildQuantityInlineKeyboard(Long productId, int quantity) {
        InlineKeyboardButton minusButton = InlineKeyboardButton.builder()
                .text("➖")
                .callbackData("QTY_DEC:" + productId)
                .build();

        InlineKeyboardButton quantityButton = InlineKeyboardButton.builder()
                .text(" " + quantity + " ")
                .callbackData("noop") // просто отображение, без действия
                .build();

        InlineKeyboardButton plusButton = InlineKeyboardButton.builder()
                .text("➕")
                .callbackData("QTY_INC:" + productId)
                .build();

        InlineKeyboardButton addToCartButton = InlineKeyboardButton.builder()
                .text("🛒 Додати")
                .callbackData("ADD_TO_CART:" + productId)
                .build();

        // Ряд 1: ➖ quantity ➕
        InlineKeyboardRow row1 = new InlineKeyboardRow();
        row1.add(minusButton);
        row1.add(quantityButton);
        row1.add(plusButton);

        // Ряд 2: 🛒 Додати
        InlineKeyboardRow row2 = new InlineKeyboardRow();
        row2.add(addToCartButton);

        // ✅ Вот здесь создаётся rows
        List<InlineKeyboardRow> rows = new ArrayList<>();
        rows.add(row1);
        rows.add(row2);

        return new InlineKeyboardMarkup(rows);
    }

    private InlineKeyboardMarkup buildCartActionsKeyboard(Long orderId) {
        InlineKeyboardButton clearBtn = InlineKeyboardButton.builder()
                .text("🧹 Очистити кошик")
                .callbackData("CART_CLEAR:" + orderId)
                .build();

        InlineKeyboardButton checkoutBtn = InlineKeyboardButton.builder()
                .text("✅ Оформити замовлення")
                .callbackData("CART_CHECKOUT:" + orderId)
                .build();

        InlineKeyboardRow row = new InlineKeyboardRow(clearBtn, checkoutBtn);

        return InlineKeyboardMarkup.builder()
                .keyboardRow(row)
                .build();
    }


    /**
     * Sends text reply to user.
     *
     * @param sendMessage reply to a user.
     */
    private void executeMessage(SendMessage sendMessage) {
        try {
            telegramClient.execute(sendMessage);
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }

    /**
     * Sends photo reply to user.
     *
     * @param sendPhoto reply to a user.
     */
    private void executeMessage(SendPhoto sendPhoto) {
        try {
            telegramClient.execute(sendPhoto);
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }
}
