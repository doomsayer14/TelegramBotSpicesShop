package yes.shef.telegramshop.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import yes.shef.telegramshop.dto.ProductDto;
import yes.shef.telegramshop.mapper.ProductMapper;
import yes.shef.telegramshop.service.ProductService;
import yes.shef.telegramshop.entity.Product;

import java.util.List;

/**
 * {@link Product} management via HTTP server, crud operations.
 */
@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    private final ProductMapper productMapper;

    @PostMapping("/")
    public ResponseEntity<ProductDto> createProduct(@RequestBody ProductDto productDto) {
        Product product = productService.createProduct(productDto);
        ProductDto createdProduct = productMapper.productToProductDto(product);

        return ResponseEntity.created(ServletUriComponentsBuilder.fromCurrentRequest()
                        .path("/{productId}")
                        .buildAndExpand(createdProduct.getId())
                        .toUri())
                .body(createdProduct);
    }

    @GetMapping("/")
    public ResponseEntity<List<ProductDto>> getAllProducts() {
        List<ProductDto> productDtoList = productService.getAllProducts()
                .stream()
                .map(productMapper::productToProductDto)
                .toList();
        return ResponseEntity.ok(productDtoList);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ProductDto> getProduct(@PathVariable String productId) {
        Product product = productService.getProductById(Long.parseLong(productId));
        ProductDto productDto = productMapper.productToProductDto(product);
        return ResponseEntity.ok(productDto);
    }

    @PutMapping("/")
    public ResponseEntity<ProductDto> updateProduct(@RequestBody ProductDto productDto) {
        Product updatedProduct = productService.updateProduct(productDto);
        ProductDto updatedProductDto = productMapper.productToProductDto(updatedProduct);
        return ResponseEntity.ok(updatedProductDto);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<ProductDto> deleteProduct(@PathVariable String productId) {
        productService.deleteProductById(Long.parseLong(productId));
        return ResponseEntity.noContent().build();
    }
}
