package yes.shief.telegrambotspicesshop.telegram.consumer;

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
@Slf4j
@Component
public class UpdateConsumer implements LongPollingSingleThreadUpdateConsumer {

    /**
     * Static text.
     */

    private static final String START_MESSAGE = "Привіт! Мене звати Ірина Єсьман." + System.lineSeparator() +
            "Це мій телеграм магазин спецій. Тут ви можете обрати будь-який" +
            "товар з каталогу за своїм смаком.";

    private static final String TERMS_OF_USE = "Правила доставки";

    private static final String ABOUT_ME = "Про мене";

    private static final String DEFAULT_ANSWER = "Я вас не зрозумів.";

    /**
     * Injections.
     */

    private final TelegramService telegramService;

    private final SpiceService spiceService;

    public UpdateConsumer(TelegramService telegramService, SpiceService spiceService) {
        this.telegramService = telegramService;
        this.spiceService = spiceService;
    }

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

            //this stream and is-statement checks whe
            Optional<Spice> optionalSpice = spiceList.stream()
                    .filter(spice -> messageText.equalsIgnoreCase(spice.getName()))
                    .findFirst();
            if (optionalSpice.isPresent()) {
                Spice spice = optionalSpice.get();
                telegramService.sendSpice(spice);
            } else {
                switch (messageText) {
                    case "/start" -> telegramService.sendStartMenu(chatId, START_MESSAGE);
                    case "Каталог товарів" -> telegramService.sendCatalogue(chatId);
                    case "Мій кошик" -> telegramService.sendCart(chatId);
                    case "Умови доставки" -> telegramService.sendTermsOfUse(chatId, TERMS_OF_USE);
                    case "Про мене" -> telegramService.sendAboutMe(chatId, ABOUT_ME);
                    case default -> telegramService.sendMessage(chatId, DEFAULT_ANSWER);
                }
            }
        }
    }

}
