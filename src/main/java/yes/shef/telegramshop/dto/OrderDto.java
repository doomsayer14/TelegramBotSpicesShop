package yes.shef.telegramshop.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import yes.shef.telegramshop.entity.Order;
import yes.shef.telegramshop.entity.enums.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO class for {@link Order}
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDto {
    private Long id;
    private CustomerDto customer;
    private List<OrderItemDto> orderItems;
    private OrderStatus status;
    private BigDecimal total;
    private LocalDateTime createdAt;
    private String city;
    private Integer novaPoshtaOffice;
    private String recipient;
}
