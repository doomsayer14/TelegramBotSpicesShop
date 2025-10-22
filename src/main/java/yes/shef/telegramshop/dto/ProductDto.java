package yes.shef.telegramshop.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import yes.shef.telegramshop.entity.Product;
import yes.shef.telegramshop.entity.enums.ProductType;

import java.math.BigDecimal;

/**
 * DTO class for {@link Product}
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDto {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private ProductType productType;
    private byte[] imageBytes;
}
