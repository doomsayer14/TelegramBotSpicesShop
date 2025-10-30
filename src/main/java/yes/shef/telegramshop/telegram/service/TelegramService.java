package yes.shef.telegramshop.telegram.service;

import org.telegram.telegrambots.meta.api.objects.Update;
import yes.shef.telegramshop.entity.Product;

public interface TelegramService {
    /**
     * Sends specified {@link Product} to user.
     *
     * @param chatId  in which chat this message should be sent.
     * @param product {@link Product} to be sent.
     */
    void sendProduct(Long chatId, Product product);

    /**
     * Reacts on command "/start".
     *
     * @param update  update from user.
     * @param message message to be sent.
     */
    void sendStartMenu(Update update, String message);

    void sendCategoryMenu(Long chatId);

    /**
     * Sends catalogue by specified {@link yes.shef.telegramshop.entity.enums.ProductType}.
     *
     * @param messageText {@link yes.shef.telegramshop.entity.enums.ProductType}.
     * @param chatId      in which chat this message should be sent.
     */
    void sendCatalogue(String messageText, Long chatId);

    /**
     * Sends actual user's cart.
     *
     * @param chatId in which chat this message should be sent.
     */
    void sendCart(Long chatId);

    /**
     * Sends regular message.
     *
     * @param chatId  in which chat this message should be sent.
     * @param message message to be sent.
     */
    void sendMessage(Long chatId, String message);

    void editProductQuantityMarkup(Long chatId, Integer messageId, Long productId, int quantity);

    void answerCallback(String callbackId, String text);
}
