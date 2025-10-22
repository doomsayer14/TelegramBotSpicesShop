package yes.shef.telegramshop.telegram.service.impl;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import yes.shef.telegramshop.entity.Product;
import yes.shef.telegramshop.service.ProductService;
import yes.shef.telegramshop.telegram.service.TelegramService;
import yes.shef.telegramshop.util.Commands;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Actually not a class for business logic but for some methods for working directly
 * with telegram.
 */
@Service
public class TelegramServiceImpl implements TelegramService {

    /**
     * Token from BotFather. Don't forget to clean it from application.properties.
     */
    @Value("${telegram.bot.token}")
    private String token;

    private TelegramClient telegramClient;

    private final ProductService productService;


    public TelegramServiceImpl(ProductService productService) {
        this.productService = productService;
    }

    /**
     * Need this to be sure that @Value will be instantiated before it will be used.
     */
    @PostConstruct
    public void init() {
        this.telegramClient = new OkHttpTelegramClient(token);
    }

    @Override
    public void sendSpice(Long chatId, Product product) {
        // общий текст
        String caption = product.getName()
                + System.lineSeparator() + System.lineSeparator()
                + (product.getDescription() != null ? product.getDescription() : "")
                + System.lineSeparator() + System.lineSeparator()
                + "Ціна - " + product.getPrice() + " грн.";

        byte[] bytes = product.getImageBytes();

        // есть изображение — шлём фото с подписью (ограничение Telegram: 1024 символа в caption)
        if (bytes != null && bytes.length > 0) {
            String safeCaption = caption.length() > 1024 ? caption.substring(0, 1021) + "..." : caption;

            InputFile inputFile = new InputFile(new ByteArrayInputStream(bytes), "spice.jpg");
            SendPhoto sendPhoto = SendPhoto.builder()
                    .chatId(chatId)
                    .photo(inputFile)
                    .caption(safeCaption)
                    .build();

            executeMessage(sendPhoto);
            return;
        }

        // нет изображения — шлём текстовое сообщение (до 4096 символов)
        SendMessage sendMessage = SendMessage.builder()
                .chatId(chatId)
                .text(caption)
                .build();

        executeMessage(sendMessage);
    }


    @Override
    public void sendStartMenu(Long chatId, String message) {
        SendMessage sendMessage = SendMessage.builder()
                .text(message)
                .chatId(chatId)
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
        keyboardRowList.add(new KeyboardRow(Commands.GO_TO_MAIN_MENU_COMMAND));


        SendMessage sendMessage = SendMessage.builder()
                .text(Commands.CATALOGUE_HEADER_MESSAGE)
                .chatId(chatId)
                .replyMarkup(new ReplyKeyboardMarkup(keyboardRowList))
                .build();

        executeMessage(sendMessage);
    }

    public void sendCart(Long chatId) {
    }

    @Override
    public void sendMessage(Long chatId, String message) {
        SendMessage sendMessage = SendMessage.builder()
                .text(message)
                .chatId(chatId)
                .build();

        executeMessage(sendMessage);
    }

    /**
     * Sends text reply to user.
     * @param sendMessage reply to a user.
     */
    private void executeMessage(SendMessage sendMessage) {
        try {
            telegramClient.execute(sendMessage);
        } catch (TelegramApiException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Sends photo reply to user.
     * @param sendPhoto reply to a user.
     */
    private void executeMessage(SendPhoto sendPhoto) {
        try {
            telegramClient.execute(sendPhoto);
        } catch (TelegramApiException e) {
            System.out.println(e.getMessage());
        }
    }
}
