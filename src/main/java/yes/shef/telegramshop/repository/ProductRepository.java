package yes.shef.telegramshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import yes.shef.telegramshop.entity.Product;

import java.util.Optional;

/**
 * Repository for {@link Product}.
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("SELECT p.name FROM Product p WHERE p.id = :id")
    String findNameById(Long id);

    Optional<Product> findByName(String name);
}
