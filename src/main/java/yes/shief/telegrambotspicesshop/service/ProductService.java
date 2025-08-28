package yes.shief.telegrambotspicesshop.service;

import yes.shief.telegrambotspicesshop.dto.ProductDto;
import yes.shief.telegrambotspicesshop.entity.Product;

import java.util.List;

/**
 * Business logic implementation for {@link Product}
 */
public interface ProductService {

    /**
     * Finds all the products from DB.
     * @return list of all products.
     */
   List<Product> getAllProducts();

    /**
     * Creates new {@link Product} from DTO.
     * @param productDto product to be created.
     * @return created product.
     */
    Product createProduct(ProductDto productDto);

    /**
     * Get product by id.
     * @param productId id of product to be got.
     * @return product with specified id.
     */
    Product getProductById(Long productId);

    /**
     * Updates {@link Product} from DTO.
     * @param productDto product to be updated.
     * @return updated product
     */
    Product updateProduct(ProductDto productDto);

    /**
     * Deletes product by id.
     * @param productId id of product to be deleted.
     * @return deleted product.
     */
    void deleteProductById(Long productId);
}
