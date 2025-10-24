package yes.shef.telegramshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import yes.shef.telegramshop.entity.OrderItem;

import java.math.BigDecimal;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    @Query("""
                select coalesce(sum(oi.price * oi.quantity), 0)
                from OrderItem oi
                where oi.order.id = :orderId
            """)
    BigDecimal calcTotalByOrderId(@Param("orderId") Long orderId);

    @Modifying
    @Transactional
    @Query("delete from OrderItem oi where oi.order.id = :orderId")
    void deleteByOrderId(@Param("orderId") Long orderId);

}

