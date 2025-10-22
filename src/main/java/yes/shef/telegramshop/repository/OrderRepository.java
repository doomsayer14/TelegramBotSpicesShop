package yes.shef.telegramshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import yes.shef.telegramshop.entity.Order;

/**
 * Repository for {@link Order}.
 */
public interface OrderRepository extends JpaRepository<Order, Long> {
}
