package yes.shef.telegramshop.mapper;

import org.springframework.stereotype.Component;
import yes.shef.telegramshop.dto.OrderItemDto;
import yes.shef.telegramshop.entity.OrderItem;

/**
 * Mapper for {@link OrderItem} in facade style.
 */
@Component
public class OrderItemMapper {

    /**
     * Maps {@link OrderItem} to {@link OrderItemDto}.
     *
     * @param orderItem to be mapped to {@link OrderItemDto}.
     * @return new Dto.
     */
    public OrderItemDto orderItemToOrderItemDto(OrderItem orderItem) {
        return OrderItemDto.builder()
                .id(orderItem.getId())
                .productId(orderItem.getProduct().getId())
                .quantity(orderItem.getQuantity())
                .price(orderItem.getPrice())
                .build();
    }
}
