package yes.shief.telegrambotspicesshop.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import lombok.Data;

@Data
@Entity
public class Spice {
    @Id
    private Long id;

    @Column
    private String name;

    @Column
    private String description;

    /**
     * Spice picture.
     */
    @Lob
    @Column
    private byte[] imageBytes;
}
