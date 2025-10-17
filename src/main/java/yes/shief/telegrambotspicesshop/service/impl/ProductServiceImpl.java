package yes.shief.telegrambotspicesshop.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import yes.shief.telegrambotspicesshop.dto.ProductDto;
import yes.shief.telegrambotspicesshop.entity.Product;
import yes.shief.telegrambotspicesshop.exception.ProductNotFoundException;
import yes.shief.telegrambotspicesshop.repository.ProductRepository;
import yes.shief.telegrambotspicesshop.service.ProductService;

import java.util.List;

/**
 * Implementation for {@link ProductService}.
 */
@Slf4j
@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public Product createProduct(ProductDto productDto) {
        Product product = Product.builder()
                .name(productDto.getName())
                .description(productDto.getDescription())
                .price(productDto.getPrice())
                .imageBytes(productDto.getImageBytes())
                .build();

        log.info("Saving new product: {}", product);
        return productRepository.save(product);
    }

    @Override
    public Product getProductById(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() ->
                        new ProductNotFoundException("Product can't be found for id = " + productId));
    }

    @Override
    public Product updateProduct(ProductDto productDto) {
        Product product = getProductById(productDto.getId());

        //TODO:update product fields.

        log.info("Updating product: {}", product);
        return productRepository.save(null);
    }

    @Override
    public void deleteProductById(Long productId) {
        productRepository.deleteById(productId);
        log.info("Deleting product: {}", productId);
    }
}