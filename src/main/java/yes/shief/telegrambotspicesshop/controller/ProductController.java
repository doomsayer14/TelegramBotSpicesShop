package yes.shief.telegrambotspicesshop.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import yes.shief.telegrambotspicesshop.dto.ProductDto;
import yes.shief.telegrambotspicesshop.entity.Product;
import yes.shief.telegrambotspicesshop.mapper.ProductMapper;
import yes.shief.telegrambotspicesshop.service.ProductService;

import java.util.List;

/**
 * Spices management via HTTP server, crud operations.
 */
@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    private final ProductMapper productMapper;

    @PostMapping("")
    public ResponseEntity<ProductDto> createProduct(@RequestBody ProductDto productDto) {
        Product product = productService.createProduct(productDto);
        ProductDto createdSpice = productMapper.spiceToSpiceDto(product);

        return ResponseEntity.created(ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{productId}")
                .buildAndExpand(createdSpice.getId())
                .toUri())
                .body(createdSpice);
    }

    @GetMapping("/")
    public ResponseEntity<List<ProductDto>> getAllProducts() {
        List<ProductDto> productDtoList = productService.getAllProducts()
                .stream()
                .map(productMapper::spiceToSpiceDto)
                .toList();
        return ResponseEntity.ok(productDtoList);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ProductDto> getProduct(@PathVariable String productId) {
        Product product = productService.getProductById(Long.parseLong(productId));
        ProductDto productDto = productMapper.spiceToSpiceDto(product);
        return ResponseEntity.ok(productDto);
    }

    @PutMapping("/")
    public ResponseEntity<ProductDto> updateProduct(@RequestBody ProductDto productDto) {
        Product updatedProduct = productService.updateProduct(productDto);
        ProductDto updatedProductDto = productMapper.spiceToSpiceDto(updatedProduct);
        return ResponseEntity.ok(updatedProductDto);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<ProductDto> deleteProduct(@PathVariable String productId) {
        productService.deleteProductById(Long.parseLong(productId));
        return ResponseEntity.noContent().build();
    }
}
