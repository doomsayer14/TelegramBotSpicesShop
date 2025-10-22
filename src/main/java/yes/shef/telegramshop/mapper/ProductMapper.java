package yes.shef.telegramshop.mapper;

import org.springframework.stereotype.Component;
import yes.shef.telegramshop.dto.ProductDto;
import yes.shef.telegramshop.entity.Product;

/**
 * Mapper for {@link Product} in facade style.
 */
@Component
public class ProductMapper {
    /**
     * Maps {@link Product} to {@link ProductDto}.
     *
     * @param product to be mapped to {@link ProductDto}.
     * @return new Dto.
     */
    public ProductDto productToProductDto(Product product) {
        return ProductDto.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .productType(product.getProductType())
                .imageBytes(product.getImageBytes())
                .build();
    }
}
