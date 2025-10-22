package yes.shef.telegramshop.entity.enums;

import yes.shef.telegramshop.entity.Order;

/**
 * Statuses of {@link Order}.
 * {@link OrderStatus#PAID} - the order is already being paid.
 * {@link OrderStatus#UNPAID} - the order hasn't being paid yet.
 */
public enum OrderStatus {
    PAID, UNPAID
}