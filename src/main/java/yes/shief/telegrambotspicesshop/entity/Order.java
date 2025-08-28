package yes.shief.telegrambotspicesshop.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import yes.shief.telegrambotspicesshop.entity.enums.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Entity for ordering.
 */
@Table(name = "order")
@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    @Id
    private Long id;

    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    private Customer owner;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status = OrderStatus.UNPAID;

    @Column(nullable = false)
    private BigDecimal total = BigDecimal.ZERO;

    @CreatedDate
    private LocalDateTime createdAt;
}
