package yes.shief.telegrambotspicesshop.telegram.consumer;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.util.List;
import java.util.Objects;

@Slf4j
@Component
public class UpdateConsumer implements LongPollingSingleThreadUpdateConsumer {

    @Value("${telegram.bot.token}")
    private String token;

    private TelegramClient telegramClient;

    @PostConstruct
    public void init() {
        this.telegramClient = new OkHttpTelegramClient(token);
    }

    @Override
    public void consume(Update update) {

        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            Long chatId = update.getMessage().getChatId();

            if (Objects.equals(messageText, "/start")) {
                sendMainMenu(chatId);
            } else if (Objects.equals(messageText, "/keyboard")) {
                sendReplyKeyboard(chatId);
            } else if (Objects.equals(messageText, "Привіт")) {
                sendMyName(chatId, update.getMessage().getFrom());
            } else if (Objects.equals(messageText, "Зоображення")) {
                sendPicture(chatId);
            } else {
                sendMessage(chatId, "Напишіть команду \"/start\".");
            }
        } else if (update.hasCallbackQuery()) {
            handleCallbackQuery(update.getCallbackQuery());
        }


    }

    private void sendReplyKeyboard(Long chatId) {
        SendMessage sendMessage = SendMessage.builder()
                .text("Приклад клавіатури")
                .chatId(chatId)
                .build();

        List<KeyboardRow> keyboardRowList = List.of(
                new KeyboardRow("Привіт", "Зоображення")
        );

        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup(keyboardRowList);
        sendMessage.setReplyMarkup(replyKeyboardMarkup);

        try {
            telegramClient.execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    private void handleCallbackQuery(CallbackQuery callbackQuery) {
        var data = callbackQuery.getData();
        var chatId = callbackQuery.getFrom().getId();
        var user = callbackQuery.getFrom();

        switch (data) {
            case "my_name" -> sendMyName(chatId, user);
            case "random_num" -> sendRandomNum(chatId);
            case "picture" -> sendPicture(chatId);
            default -> sendMessage(chatId, "Невідома команда!");
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
            System.out.println("error");
        }
    }

    private void sendPicture(Long chatId) {
        sendMessage(chatId, "Зоображення.");
    }

    private void sendRandomNum(Long chatId) {
        sendMessage(chatId, "Номер.");
    }

    private void sendMyName(Long chatId, User user) {
        sendMessage(chatId, "Вас звуть " + user.getFirstName() + " " + user.getLastName());

    }

    private void sendMainMenu(Long chatId) {
        SendMessage sendMessage = SendMessage.builder()
                .text("Привіт! Вибери дію:")
                .chatId(chatId)
                .build();

        var buttonName = InlineKeyboardButton.builder()
                .text("Як мене звуть?")
                .callbackData("my_name")
                .build();

        var buttonNum = InlineKeyboardButton.builder()
                .text("Випадкове число")
                .callbackData("random_num")
                .build();

        var buttonPic = InlineKeyboardButton.builder()
                .text("Зоображення")
                .callbackData("picture")
                .build();

        List<InlineKeyboardRow> inlineKeyboardRowList = List.of(
                new InlineKeyboardRow(buttonName),
                new InlineKeyboardRow(buttonNum),
                new InlineKeyboardRow(buttonPic)
        );

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup(inlineKeyboardRowList);

        sendMessage.setReplyMarkup(inlineKeyboardMarkup);
        try {
            telegramClient.execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
}
