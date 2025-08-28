package yes.shief.telegrambotspicesshop.dto;

import lombok.Builder;
import lombok.Data;
import yes.shief.telegrambotspicesshop.entity.Product;
import yes.shief.telegrambotspicesshop.entity.enums.ProductType;

import java.math.BigDecimal;

/**
 * DTO class for {@link Product}
 */
@Data
@Builder
public class ProductDto {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private ProductType productType;
    private byte[] imageBytes;
}
