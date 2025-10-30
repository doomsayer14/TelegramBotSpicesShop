package yes.shef.telegramshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import yes.shef.telegramshop.entity.Product;
import yes.shef.telegramshop.entity.enums.ProductType;

import java.util.List;
import java.util.Optional;

/**
 * Repository for {@link Product}.
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    Optional<Product> findByName(String name);

    List<Product> findAllByProductType(ProductType type);
}
