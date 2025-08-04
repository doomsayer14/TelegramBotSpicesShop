package yes.shief.telegrambotspicesshop.telegram.keyboard.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents start keyboard buttons. We are saving them to have an opportunity to change them
 * dynamically by admin tools.
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StartKeyboardButton {
    @Id
    private Long id;

    @Column(unique = true)
    private String keyboardButtonName;
}