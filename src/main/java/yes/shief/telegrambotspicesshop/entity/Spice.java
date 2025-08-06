package yes.shief.telegrambotspicesshop.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Table(name = "spices")
@Data
@Entity
public class Spice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column
    private String description;

    @Column
    private BigDecimal price;

    /**
     * Spice picture.
     */
    @Lob
    @Column
    private byte[] imageBytes;
}
