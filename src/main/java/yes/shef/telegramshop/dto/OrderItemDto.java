package yes.shef.telegramshop.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import yes.shef.telegramshop.entity.OrderItem;

import java.math.BigDecimal;

/**
 * DTO class for {@link OrderItem}
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemDto {
    private Long id;
    private Long productId;
    private Integer quantity;
    private BigDecimal price;
}