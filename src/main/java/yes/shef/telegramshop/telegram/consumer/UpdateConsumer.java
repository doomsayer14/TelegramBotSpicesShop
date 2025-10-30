package yes.shef.telegramshop.telegram.consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.EnumUtils;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.objects.Update;
import yes.shef.telegramshop.entity.Product;
import yes.shef.telegramshop.service.ProductService;
import yes.shef.telegramshop.telegram.service.TelegramService;
import yes.shef.telegramshop.util.Commands;

import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
@Component
public class UpdateConsumer implements LongPollingSingleThreadUpdateConsumer {

    private final TelegramService telegramService;
    private final ProductService productService;
    private final CallbackConsumer callbackConsumer;

    @Override
    public void consume(Update update) {
        if (callbackConsumer.consume(update)) {
            return;
        }

        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            Long chatId = update.getMessage().getChatId();

            if (messageText.equals(Commands.SPICE_MESSAGE) ||
                    messageText.equals(Commands.BOX_MESSAGE)) {
                telegramService.sendCatalogue(messageText, chatId);
                return;
            }
            if (isProductName(messageText, chatId)) {
                return;
            }

            switch (messageText) {
                case Commands.START_COMMAND -> telegramService.sendStartMenu(update, Commands.START_MESSAGE);
                case Commands.CATALOGUE_COMMAND -> telegramService.sendCategoryMenu(chatId);
                case Commands.MY_CART_COMMAND -> telegramService.sendCart(chatId);
                case Commands.TERMS_OF_USE_COMMAND ->
                        telegramService.sendMessage(chatId, Commands.TERMS_OF_USE_MESSAGE);
                case Commands.ABOUT_ME_COMMAND -> telegramService.sendMessage(chatId, Commands.ABOUT_ME_MESSAGE);
                case Commands.GO_TO_MAIN_MENU_COMMAND ->
                        telegramService.sendStartMenu(update, Commands.GO_TO_MAIN_MENU_MESSAGE);
                default -> telegramService.sendMessage(chatId, Commands.DEFAULT_ANSWER);
            }
        }
    }

    private boolean isProductName(String messageText, Long chatId) {
        Optional<Product> optionalProduct = productService.getProductByName(messageText);

        if (optionalProduct.isPresent()) {
            Product product = optionalProduct.get();
            callbackConsumer.setQuantity(chatId, product.getId(), 1);
            telegramService.sendProduct(chatId, product);
            return true;
        }
        return false;
    }
}
