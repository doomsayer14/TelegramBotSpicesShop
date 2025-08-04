package yes.shief.telegrambotspicesshop.telegram.longpollingbot;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.longpolling.interfaces.LongPollingUpdateConsumer;
import org.telegram.telegrambots.longpolling.starter.SpringLongPollingBot;
import yes.shief.telegrambotspicesshop.telegram.consumer.UpdateConsumer;

/**
 * Entry point
 */
@Component
public class SpicesShopTelegramBot implements SpringLongPollingBot {

    /**
     * Token from BotFather. Don't forget to clean it from application.properties.
     */
    @Value("${telegram.bot.token}")
    private String token;

    private final UpdateConsumer updateConsumer;

    public SpicesShopTelegramBot(UpdateConsumer updateConsumer) {
        this.updateConsumer = updateConsumer;
    }

    @Override
    public String getBotToken() {
        return token;
    }

    /**
     * {@link UpdateConsumer} is a class with a main logic.
     * @return
     */
    @Override
    public LongPollingUpdateConsumer getUpdatesConsumer() {
        return updateConsumer;
    }
}
