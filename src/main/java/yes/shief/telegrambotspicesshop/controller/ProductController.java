package yes.shief.telegrambotspicesshop.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import yes.shief.telegrambotspicesshop.dto.ProductDto;
import yes.shief.telegrambotspicesshop.entity.Product;
import yes.shief.telegrambotspicesshop.mapper.ProductMapper;
import yes.shief.telegrambotspicesshop.service.ProductService;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final ProductMapper productMapper;

    // --- LIST ---
    @GetMapping("/")
    public String getAllProducts(Model model) {
        List<ProductDto> products = productService.getAllProducts()
                .stream()
                .map(productMapper::spiceToSpiceDto)
                .toList();
        model.addAttribute("products", products);
        return "products";
    }

    // --- CREATE FORM ---
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("product", new ProductDto());
        return "product_form";
    }

    // --- CREATE ---
    @PostMapping("")
    public String createProduct(@ModelAttribute ProductDto productDto,
                                @RequestParam("imageFile") MultipartFile imageFile) throws IOException {

        if (imageFile != null && !imageFile.isEmpty()) {
            productDto.setImageBytes(imageFile.getBytes());
        }
        Product product = productService.createProduct(productDto);
        return "redirect:/product/";
    }

    // --- EDIT FORM ---
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        Product product = productService.getProductById(id);
        ProductDto productDto = productMapper.spiceToSpiceDto(product);
        model.addAttribute("product", productDto);
        return "product_form";
    }

    // --- UPDATE ---
    @PostMapping(value = "/")
    public String updateProduct(@ModelAttribute ProductDto productDto,
                                @RequestParam("imageFile") MultipartFile imageFile) throws IOException {
        if (imageFile != null && !imageFile.isEmpty()) {
            productDto.setImageBytes(imageFile.getBytes());
        }
        productService.updateProduct(productDto);
        return "redirect:/product/";
    }

    // --- DELETE ---
    @PostMapping("/{id}")
    public String deleteProduct(@PathVariable("id") Long id) {
        productService.deleteProductById(id);
        return "redirect:/product/";
    }

    // --- VIEW ONE ---
    @GetMapping("/{id}")
    public String viewProduct(@PathVariable("id") Long id, Model model) {
        Product product = productService.getProductById(id);
        ProductDto productDto = productMapper.spiceToSpiceDto(product);
        model.addAttribute("product", productDto);
        return "product_view";
    }
}
