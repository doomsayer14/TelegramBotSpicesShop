package yes.shef.telegramshop.telegram.consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.objects.Update;
import yes.shef.telegramshop.entity.Product;
import yes.shef.telegramshop.service.ProductService;
import yes.shef.telegramshop.telegram.service.TelegramService;
import yes.shef.telegramshop.util.Commands;

import java.util.List;
import java.util.Optional;

/**
 * Main request handler.
 */
@RequiredArgsConstructor
@Slf4j
@Component
public class UpdateConsumer implements LongPollingSingleThreadUpdateConsumer {

    private final TelegramService telegramService;

    private final ProductService productService;

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


            List<Product> productList = productService.getAllProducts();

            //this stream and if-statement checks whether message has a spice name or not
            //TODO: move this logic to SQL.
            Optional<Product> optionalSpice = productList.stream()
                    .filter(spice -> messageText.equalsIgnoreCase(spice.getName()))
                    .findFirst();
            if (optionalSpice.isPresent()) {
                Product product = optionalSpice.get();
                telegramService.sendSpice(chatId, product);
            } else {
                switch (messageText) {
                    case Commands.START_COMMAND -> telegramService.sendStartMenu(chatId, Commands.START_MESSAGE);
                    case Commands.CATALOGUE_COMMAND -> telegramService.sendCatalogue(chatId);
                    case Commands.MY_CART_COMMAND -> telegramService.sendCart(chatId);
                    case Commands.TERMS_OF_USE_COMMAND -> telegramService.sendMessage(chatId, Commands.TERMS_OF_USE_MESSAGE);
                    case Commands.ABOUT_ME_COMMAND -> telegramService.sendMessage(chatId, Commands.ABOUT_ME_MESSAGE);
                    case Commands.GO_TO_MAIN_MENU_COMMAND -> telegramService.sendStartMenu(chatId, Commands.GO_TO_MAIN_MENU_MESSAGE);
                    default -> telegramService.sendMessage(chatId, Commands.DEFAULT_ANSWER);
                }
            }
        }
    }
}