package yes.shef.telegramshop.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yes.shef.telegramshop.dto.OrderDto;
import yes.shef.telegramshop.entity.Customer;
import yes.shef.telegramshop.entity.Order;
import yes.shef.telegramshop.entity.OrderItem;
import yes.shef.telegramshop.entity.enums.OrderStatus;
import yes.shef.telegramshop.exception.CustomerNotFoundException;
import yes.shef.telegramshop.exception.OrderNotFoundException;
import yes.shef.telegramshop.repository.CustomerRepository;
import yes.shef.telegramshop.repository.OrderItemRepository;
import yes.shef.telegramshop.repository.OrderRepository;
import yes.shef.telegramshop.service.OrderService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implementation for {@link OrderService}.
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final OrderItemRepository orderItemRepository;

    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAllWithItemsAndProducts();
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
    @Transactional
    public void addToOrder(Long orderId, OrderItem orderItem) {
        Order order = orderRepository.findByIdFetchItems(orderId).orElseThrow();

        order.addItem(orderItem);

        BigDecimal total = calculateTotal(orderId);
        order.setTotal(total);

        orderRepository.save(order);
    }

    @Override
    @Transactional
    public void emptyOrder(Long orderId) {
        // Удаляем все позиции одним запросом — без инициализации orderItems
        orderItemRepository.deleteByOrderId(orderId);

        // Сбрасываем итог и сохраняем заказ
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found: " + orderId));
        order.setTotal(BigDecimal.ZERO);
        orderRepository.save(order);

        log.info("Emptied order: {}", orderId);
    }

    @Override
    @Transactional
    public Order getOrCreateUnpaidOrder(Long customerId) {
        return orderRepository.findByCustomerIdAndStatusFetchItems(customerId, OrderStatus.UNPAID)
                .orElseGet(() -> orderRepository.save(
                        Order.builder()
                                .customer(Customer.builder().id(customerId).build())
                                .status(OrderStatus.UNPAID)
                                .orderItems(new ArrayList<>())
                                .total(BigDecimal.ZERO)
                                .build()
                ));
    }

    @Override
    public Optional<Order> getUnpaidOrder(Long customerId) {
        return orderRepository.findFirstByCustomerIdAndStatusOrderByIdDesc(customerId, OrderStatus.UNPAID);
    }

    /**
     * Calculates total order price.
     *
     * @param orderId id of the {@link Order} to be recalculated.
     * @return total price.
     */
    private BigDecimal calculateTotal(Long orderId) {
        return orderItemRepository.calcTotalByOrderId(orderId);
    }

}
