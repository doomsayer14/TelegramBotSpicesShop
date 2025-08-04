package yes.shief.telegrambotspicesshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import yes.shief.telegrambotspicesshop.entity.Spice;

/**
 * Repository for {@link Spice}.
 */
@Repository
public interface SpiceRepository extends JpaRepository<Spice, Long> {
}
