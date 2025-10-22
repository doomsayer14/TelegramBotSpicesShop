package yes.shef.telegramshop.service;

import yes.shef.telegramshop.dto.OrderDto;
import yes.shef.telegramshop.entity.Order;
import yes.shef.telegramshop.entity.OrderItem;

import java.util.List;

/**
 * Business logic implementation for {@link Order}
 */
public interface OrderService {

    /**
     * Finds all the orders from DB.
     *
     * @return list of all orders.
     */
    List<Order> getAllOrders();

    /**
     * Creates new {@link Order} from DTO.
     *
     * @param orderDto order to be created.
     * @return created order.
     */
    Order createOrder(OrderDto orderDto);

    /**
     * Get order by id.
     *
     * @param orderId id of order to be got.
     * @return order with specified id.
     */
    Order getOrderById(Long orderId);

    /**
     * Deletes order by id.
     *
     * @param orderId id of order to be deleted.
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
     * Empties whole order.
     *
     * @param orderId id of the {@link Order} to be emptied.
     */
    void emptyOrder(Long orderId);
}
