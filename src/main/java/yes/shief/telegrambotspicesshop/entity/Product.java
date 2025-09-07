package yes.shief.telegrambotspicesshop.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import yes.shief.telegrambotspicesshop.entity.enums.ProductType;

import java.math.BigDecimal;

/**
 * Entity for any good in shop.
 */
@Table(name = "products")
@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column
    private String description;

    @Column
    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProductType productType;

    /**
     * Spice picture.
     */
    @Lob
    @Column
    private byte[] imageBytes;
}
