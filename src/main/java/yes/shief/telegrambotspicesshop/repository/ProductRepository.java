package yes.shief.telegrambotspicesshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import yes.shief.telegrambotspicesshop.entity.Product;

/**
 * Repository for {@link Product}.
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
}
