package yes.shief.telegrambotspicesshop.telegram.longpollingbot;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.longpolling.interfaces.LongPollingUpdateConsumer;
import org.telegram.telegrambots.longpolling.starter.SpringLongPollingBot;
import yes.shief.telegrambotspicesshop.telegram.consumer.UpdateConsumer;

@Component
public class SpicesShopTelegramBot implements SpringLongPollingBot {

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

    @Override
    public LongPollingUpdateConsumer getUpdatesConsumer() {
        return updateConsumer;
    }
}
