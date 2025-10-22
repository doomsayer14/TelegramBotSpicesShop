package yes.shef.telegramshop.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import yes.shef.telegramshop.entity.enums.ProductType;

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
    @Column
    private ProductType productType;

    /**
     * Spice picture.
     */
    @Lob
    @Column
    private byte[] imageBytes;
}
