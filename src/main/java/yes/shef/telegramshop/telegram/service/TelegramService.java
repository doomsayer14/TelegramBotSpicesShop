package yes.shef.telegramshop.telegram.service;

import yes.shef.telegramshop.entity.Product;

public interface TelegramService {
    /**
     * Sends specified spice to user.
     * @param chatId in which chat this message should be sent.
     * @param product spice to be sent.
     */
    void sendSpice(Long chatId, Product product);

    /**
     * Reacts on command "/start".
     * @param chatId in which chat this message should be sent.
     * @param message message to be sent.
     */
    void sendStartMenu(Long chatId, String message);

    /**
     *
     * @param chatId in which chat this message should be sent.
     */
    void sendCatalogue(Long chatId);

    /**
     * Sends actual user's cart.
     * @param chatId in which chat this message should be sent.
     */
    void sendCart(Long chatId);

    /**
     * Sends regular message.
     * @param chatId in which chat this message should be sent.
     * @param message message to be sent.
     */
    void sendMessage(Long chatId, String message);
}
