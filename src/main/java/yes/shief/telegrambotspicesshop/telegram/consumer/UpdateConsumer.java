package yes.shief.telegrambotspicesshop.telegram.consumer;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import yes.shief.telegrambotspicesshop.telegram.keyboard.service.KeyboardService;

/**
 * Main request handler
 */
@Slf4j
@Component
public class UpdateConsumer implements LongPollingSingleThreadUpdateConsumer {

    @Value("${telegram.bot.token}")
    private String token;

    private TelegramClient telegramClient;

    private KeyboardService keyboardService;

    public UpdateConsumer(KeyboardService keyboardService) {
        this.keyboardService = keyboardService;
    }

    @PostConstruct
    public void init() {
        this.telegramClient = new OkHttpTelegramClient(token);
    }

    @Override
    public void consume(Update update) {

        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            Long chatId = update.getMessage().getChatId();

            switch (messageText) {
                case "/start" -> sendStartMenu(chatId);
                default -> sendMessage(chatId, "Я вас не зрозумів");
            }

        }

    }

    private void sendMessage(Long chatId, String message) {
        SendMessage sendMessage = SendMessage.builder()
                .text(message)
                .chatId(chatId)
                .build();

        try {
            telegramClient.execute(sendMessage);
        } catch (TelegramApiException e) {
            System.out.println(e.getMessage());
        }
    }

    private void sendStartMenu(Long chatId) {
        SendMessage sendMessage = SendMessage.builder()
                .text("Привіт! Мене звати Ірина Єсьман." + System.lineSeparator() +
                        "Це мій телеграм магазин спецій. Тут ви можете обрати будь-який" +
                        "товар з каталогу за своїм смаком.")
                .chatId(chatId)
                .build();

        keyboardService.getStartKeyboard();

        try {
            telegramClient.execute(sendMessage);
        } catch (TelegramApiException e) {
            System.out.println(e.getMessage());
        }
    }
}
