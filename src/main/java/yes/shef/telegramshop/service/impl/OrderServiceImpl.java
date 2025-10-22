package yes.shef.telegramshop.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import yes.shef.telegramshop.dto.OrderDto;
import yes.shef.telegramshop.entity.Customer;
import yes.shef.telegramshop.entity.Order;
import yes.shef.telegramshop.entity.OrderItem;
import yes.shef.telegramshop.entity.enums.OrderStatus;
import yes.shef.telegramshop.exception.CustomerNotFoundException;
import yes.shef.telegramshop.exception.OrderNotFoundException;
import yes.shef.telegramshop.repository.CustomerRepository;
import yes.shef.telegramshop.repository.OrderRepository;
import yes.shef.telegramshop.repository.ProductRepository;
import yes.shef.telegramshop.service.OrderService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation for {@link OrderService}.
 */
@Slf4j
@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    private final CustomerRepository customerRepository;

    private final ProductRepository productRepository;

    public OrderServiceImpl(OrderRepository orderRepository, CustomerRepository customerRepository, ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.customerRepository = customerRepository;
        this.productRepository = productRepository;
    }

    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    public Order createOrder(OrderDto orderDto) {
        Customer customer = customerRepository.findById(orderDto.getCustomer().getId())
                .orElseThrow(() -> new CustomerNotFoundException
                        ("Customer can't be found for id = " + orderDto.getCustomer().getId()));

        Order order = Order.builder()
                .customer(customer)
                .status(OrderStatus.UNPAID)
                .total(BigDecimal.ZERO) // временно; ниже можем пересчитать, если null
                .createdAt(LocalDateTime.now())
                .city(orderDto.getCity())
                .novaPoshtaOffice(orderDto.getNovaPoshtaOffice())
                .recipient(orderDto.getRecipient())
                .orderItems(new ArrayList<>())
                .build();

        log.info("Saving new order: {}", order);
        return orderRepository.save(order);
    }

    @Override
    public Order getOrderById(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() ->
                        new OrderNotFoundException("Order can't be found for id = " + orderId));
    }

    @Override
    public void deleteOrderById(Long orderId) {
        orderRepository.deleteById(orderId);
        log.info("Deleting order: {}", orderId);
    }

    @Override
    public void addToOrder(Long orderId, OrderItem orderItem) {
        Order order = getOrderById(orderId);
        order.getOrderItems().add(orderItem);
        order.setTotal(calculateTotal(order.getOrderItems()));
        orderRepository.save(order);
        log.info("Added order item: {}", orderItem);
    }

    @Override
    public void emptyOrder(Long orderId) {
        Order order = getOrderById(orderId);
        order.getOrderItems().removeAll(order.getOrderItems());
        order.setTotal(calculateTotal(order.getOrderItems()));
        orderRepository.save(order);
        log.info("Emptied order: {}", order);
    }

    /**
     * Calculates total order price.
     *
     * @param items items of the {@link Order}.
     * @return total price.
     */
    private BigDecimal calculateTotal(List<OrderItem> items) {
        BigDecimal total = BigDecimal.ZERO;
        if (items == null) {
            return total;
        }
        for (OrderItem item : items) {
            BigDecimal price = item.getPrice();
            if (price == null) {
                price = BigDecimal.ZERO;
            }
            Integer quantity = item.getQuantity();
            if (quantity == null) {
                quantity = 0;
            }
            BigDecimal line = price.multiply(BigDecimal.valueOf(quantity));
            total = total.add(line);
        }
        return total;
    }

}
