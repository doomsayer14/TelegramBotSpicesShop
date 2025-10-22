package yes.shef.telegramshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import yes.shef.telegramshop.entity.Product;

/**
 * Repository for {@link Product}.
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
}
