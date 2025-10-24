package yes.shef.telegramshop.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import yes.shef.telegramshop.entity.Order;
import yes.shef.telegramshop.entity.enums.OrderStatus;

import java.util.List;
import java.util.Optional;

/**
 * Repository for {@link Order}.
 */
public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("""
                select o from Order o
                left join fetch o.orderItems oi
                left join fetch oi.product
                where o.id = :orderId
            """)
    Optional<Order> findByIdFetchItems(Long orderId);

    @Query("""
                select o from Order o
                left join fetch o.orderItems oi
                left join fetch oi.product
                where o.customer.id = :customerId and o.status = :status
            """)
    Optional<Order> findByCustomerIdAndStatusFetchItems(Long customerId, OrderStatus status);

    @Query("""
              select distinct o from Order o
              left join fetch o.orderItems oi
              left join fetch oi.product p
            """)
    List<Order> findAllWithItemsAndProducts();

    @EntityGraph(attributePaths = {"orderItems", "orderItems.product"})
    Optional<Order> findFirstByCustomerIdAndStatusOrderByIdDesc(Long customerId, OrderStatus status);


}
