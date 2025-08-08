package yes.shief.telegrambotspicesshop.telegram.consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.objects.Update;
import yes.shief.telegrambotspicesshop.entity.Spice;
import yes.shief.telegrambotspicesshop.service.SpiceService;
import yes.shief.telegrambotspicesshop.telegram.service.TelegramService;

import java.util.List;
import java.util.Optional;

/**
 * Main request handler.
 */
@RequiredArgsConstructor
@Slf4j
@Component
public class UpdateConsumer implements LongPollingSingleThreadUpdateConsumer {

    /**
     * Static text.
     * If variable ends with "_COMMAND" it means it represents a button with the same name.
     * If variable ends with "_MESSAGE" it means that this text will be sent to a user as an answer.
     */

    private static final String START_COMMAND = "/start";

    private static final String START_MESSAGE = "Привіт! Мене звати Ірина Єсьман." + System.lineSeparator() +
            "Це мій телеграм магазин спецій. Тут ви можете обрати будь-який" +
            "товар з каталогу за своїм смаком.";

    private static final String CATALOGUE_COMMAND = "Каталог товарів";

    private static final String MY_CART_COMMAND = "Мій кошик";

    private static final String TERMS_OF_USE_COMMAND = "Правила доставки";

    private static final String TERMS_OF_USE_MESSAGE = "Правила доставки оферта договір бла бла бла";

    private static final String ABOUT_ME_COMMAND = "Про мене";

    private static final String ABOUT_ME_MESSAGE = "Текст про мене";

    private static final String DEFAULT_ANSWER = "Я вас не зрозумів.";

    /**
     * Injections.
     */

    private final TelegramService telegramService;

    private final SpiceService spiceService;

    /**
     * The list of commands for the bot.
     *
     * @param update message from user.
     */
    @Override
    public void consume(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            Long chatId = update.getMessage().getChatId();

            List<Spice> spiceList = spiceService.getAllSpices();

            //this stream and if-statement checks whether message has a spice name or not
            //TODO: move this logic to SQL.
            Optional<Spice> optionalSpice = spiceList.stream()
                    .filter(spice -> messageText.equalsIgnoreCase(spice.getName()))
                    .findFirst();
            if (optionalSpice.isPresent()) {
                Spice spice = optionalSpice.get();
                telegramService.sendSpice(chatId, spice);
            } else {
                switch (messageText) {
                    case START_COMMAND -> telegramService.sendStartMenu(chatId, START_MESSAGE);
                    case CATALOGUE_COMMAND -> telegramService.sendCatalogue(chatId);
                    case MY_CART_COMMAND -> telegramService.sendCart(chatId);
                    case TERMS_OF_USE_COMMAND -> telegramService.sendMessage(chatId, TERMS_OF_USE_MESSAGE);
                    case ABOUT_ME_COMMAND -> telegramService.sendMessage(chatId, ABOUT_ME_MESSAGE);
                    default -> telegramService.sendMessage(chatId, DEFAULT_ANSWER);
                }
            }
        }
    }
}