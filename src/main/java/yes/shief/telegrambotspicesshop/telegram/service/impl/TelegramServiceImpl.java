package yes.shief.telegrambotspicesshop.telegram.service.impl;

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
import yes.shief.telegrambotspicesshop.entity.Spice;
import yes.shief.telegrambotspicesshop.service.SpiceService;
import yes.shief.telegrambotspicesshop.telegram.service.TelegramService;

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

    private final SpiceService spiceService;


    public TelegramServiceImpl(SpiceService spiceService) {
        this.spiceService = spiceService;
    }

    /**
     * Need this to be sure that @Value will be instantiated before it will be used.
     */
    @PostConstruct
    public void init() {
        this.telegramClient = new OkHttpTelegramClient(token);
    }

    @Override
    public void sendSpice(Long chatId, Spice spice) {
        InputStream inputStream = new ByteArrayInputStream(spice.getImageBytes());

        SendPhoto sendPhoto = SendPhoto.builder()
                .chatId(chatId)
                .photo(new InputFile(inputStream, "spice.jpg"))
                .caption(spice.getName() + System.lineSeparator() + System.lineSeparator() +
                        spice.getDescription() + System.lineSeparator() + System.lineSeparator() +
                        "Ціна - " + spice.getPrice() + " грн.")
                .build();

        executeMessage(sendPhoto);
    }

    @Override
    public void sendStartMenu(Long chatId, String message) {
        SendMessage sendMessage = SendMessage.builder()
                .text(message)
                .chatId(chatId)
                .build();

        List<KeyboardRow> keyboardRowList = List.of(
                new KeyboardRow("Каталог товарів"),
                new KeyboardRow("Мій кошик", "Правила доставки"),
                new KeyboardRow("Про мене")
        );

        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup(keyboardRowList);
        sendMessage.setReplyMarkup(replyKeyboardMarkup);

        executeMessage(sendMessage);
    }

    public void sendCatalogue(Long chatId) {

        List<Spice> spiceList = spiceService.getAllSpices();

        List<KeyboardRow> keyboardRowList = new ArrayList<>();

        KeyboardRow keyboardRow = new KeyboardRow();
        for (int i = 0; i < spiceList.size(); i++) {
            keyboardRow.add(spiceList.get(i).getName());
            if (i == 3) {
                keyboardRowList.add(keyboardRow);
                keyboardRow = new KeyboardRow();
            }
        }
        keyboardRowList.add(new KeyboardRow("До головного меню"));

        SendMessage sendMessage = SendMessage.builder()
                .text("Тут ви можете ознайомитись з каталогом товарів⤵️")
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
