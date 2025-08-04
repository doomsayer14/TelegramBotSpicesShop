package yes.shief.telegrambotspicesshop.telegram.keyboard.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import yes.shief.telegrambotspicesshop.telegram.keyboard.entity.StartKeyboardButton;

@Repository
public interface StartKeyboardButtonRepository extends
        JpaRepository<StartKeyboardButton, Long> {
}
