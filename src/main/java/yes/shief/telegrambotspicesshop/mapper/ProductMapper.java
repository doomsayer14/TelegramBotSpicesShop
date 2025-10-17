package yes.shief.telegrambotspicesshop.mapper;

import org.springframework.stereotype.Component;
import yes.shief.telegrambotspicesshop.dto.ProductDto;
import yes.shief.telegrambotspicesshop.entity.Product;

/**
 * Mapper for {@link Product} in facade style.
 */
@Component
public class ProductMapper {
    /**
     * Maps {@link Product} to {@link ProductDto}.
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
