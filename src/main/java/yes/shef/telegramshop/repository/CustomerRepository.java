package yes.shef.telegramshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import yes.shef.telegramshop.entity.Customer;

/**
 * Repository for {@link Customer}.
 */
@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
