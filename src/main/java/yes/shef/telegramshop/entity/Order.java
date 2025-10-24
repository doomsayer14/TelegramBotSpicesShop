package yes.shef.telegramshop.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import yes.shef.telegramshop.entity.enums.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Entity for ordering.
 */
@Table(name = "orders")
@Data
@ToString(exclude = "orderItems")
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "customer", nullable = false)
    private Customer customer;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status = OrderStatus.UNPAID;

    @Column(nullable = false)
    private BigDecimal total = BigDecimal.ZERO;

    @CreatedDate
    private LocalDateTime createdAt;

    //Місто доставки
    @Column
    private String city;

    //Відділення нової пошти
    @Column
    private Integer novaPoshtaOffice;

    //Телефон отримувача
    @Column
    private Integer phoneNumber;

    //ФІО отримувача
    @Column
    private String recipient;

    public void addItem(OrderItem item) {
        item.setOrder(this);
        this.orderItems.add(item);
    }
}
