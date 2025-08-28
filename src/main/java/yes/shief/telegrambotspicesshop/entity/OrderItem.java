package yes.shief.telegrambotspicesshop.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * Entity which represents one item of {@link Order}.
 */
@Table(name = "order_item")
@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderItem {
    @Id
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;


}
