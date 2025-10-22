package yes.shef.telegramshop.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import yes.shef.telegramshop.dto.OrderDto;
import yes.shef.telegramshop.entity.Order;

/**
 * Mapper for {@link Order} in facade style.
 */
@Component
@RequiredArgsConstructor
public class OrderMapper {

    private final CustomerMapper customerMapper;

    private final OrderItemMapper orderItemMapper;

    /**
     * Maps {@link Order} to {@link OrderDto}.
     *
     * @param order to be mapped to {@link OrderDto}.
     * @return new Dto.
     */
    public OrderDto orderToOrderDto(Order order) {
        return OrderDto.builder()
                .id(order.getId())
                .customer(customerMapper.customerToCustomerDto(order.getCustomer()))
                .orderItems(order.getOrderItems().stream()
                        .map(orderItemMapper::orderItemToOrderItemDto)
                        .toList())
                .status(order.getStatus())
                .total(order.getTotal())
                .createdAt(order.getCreatedAt())
                .city(order.getCity())
                .novaPoshtaOffice(order.getNovaPoshtaOffice())
                .recipient(order.getRecipient())
                .build();
    }
}
