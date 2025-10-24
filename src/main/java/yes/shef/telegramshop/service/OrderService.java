package yes.shef.telegramshop.service;

import yes.shef.telegramshop.dto.OrderDto;
import yes.shef.telegramshop.entity.Order;
import yes.shef.telegramshop.entity.OrderItem;

import java.util.List;
import java.util.Optional;

/**
 * Business logic implementation for {@link Order}
 */
public interface OrderService {

    /**
     * Finds all the {@link Order} from DB.
     *
     * @return list of all orders.
     */
    List<Order> getAllOrders();

    /**
     * Creates new {@link Order} from DTO.
     *
     * @param orderDto order to be created.
     * @return created {@link Order}.
     */
    Order createOrder(OrderDto orderDto);

    /**
     * Get {@link Order} by id.
     *
     * @param orderId id of {@link Order} to be got.
     * @return {@link Order} with specified id.
     */
    Order getOrderById(Long orderId);

    /**
     * Deletes {@link Order} by id.
     *
     * @param orderId id of {@link Order} to be deleted.
     */
    void deleteOrderById(Long orderId);

    /**
     * Add an {@link OrderItem} to specified {@link Order}.
     *
     * @param orderId   id of the {@link Order} in which we want to add new {@link OrderItem}.
     * @param orderItem new {@link OrderItem}.
     */
    void addToOrder(Long orderId, OrderItem orderItem);

    /**
     * Empties whole {@link Order}.
     *
     * @param orderId id of the {@link Order} to be emptied.
     */
    void emptyOrder(Long orderId);

    /**
     * Gets or creates {@link Order} with {@link yes.shef.telegramshop.entity.enums.OrderStatus#UNPAID}.
     *
     * @param customerId we search for an {@link Order}
     *                   which belongs to specified {@link yes.shef.telegramshop.entity.Customer}.
     * @return {@link Order}.
     */
    Order getOrCreateUnpaidOrder(Long customerId);


    Optional<Order> getUnpaidOrder(Long customerId);
}
