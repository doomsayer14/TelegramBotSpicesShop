package yes.shef.telegramshop.service;

import yes.shef.telegramshop.dto.ProductDto;
import yes.shef.telegramshop.entity.Product;
import yes.shef.telegramshop.entity.enums.ProductType;

import java.util.List;
import java.util.Optional;

/**
 * Business logic implementation for {@link Product}
 */
public interface ProductService {

    /**
     * Finds all the {@link Product} from DB.
     *
     * @return list of all {@link Product}.
     */
    List<Product> getAllProducts();

    /**
     * Creates new {@link Product} from DTO.
     *
     * @param productDto {@link Product} to be created.
     * @return created {@link Product}.
     */
    Product createProduct(ProductDto productDto);

    /**
     * Get {@link Product} by id.
     *
     * @param productId id of {@link Product} to be got.
     * @return {@link Product} with specified id.
     */
    Product getProductById(Long productId);

    /**
     * Updates {@link Product} from DTO.
     *
     * @param productDto {@link Product} to be updated.
     * @return updated product.
     */
    Product updateProduct(ProductDto productDto);

    /**
     * Deletes {@link Product} by id.
     *
     * @param productId id of {@link Product} to be deleted.
     * @return deleted {@link Product}.
     */
    void deleteProductById(Long productId);

    /**
     * Get {@link Product} by name.
     *
     * @param name name of {@link Product} to be got.
     * @return {@link Product} with specified name.
     */
    Optional<Product> getProductByName(String name);

    /**
     * Get {@link Product} by {@link ProductType}.
     *
     * @param productType {@link ProductType} of {@link Product} to be got.
     * @return {@link List<Product>} with specified {@link ProductType}.
     */
    List<Product> getProductsByProductType(String productType);
}
