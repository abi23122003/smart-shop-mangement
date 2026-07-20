package com.smartshop.backend.controller;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.smartshop.backend.dto.ProductDTO;
import com.smartshop.backend.service.ProductService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;

 @PostMapping
   public ProductDTO saveProduct(@Valid @RequestBody ProductDTO productDTO) {
    return productService.saveProduct(productDTO);
}
@GetMapping
public List<ProductDTO> getAllProducts() {
    return productService.getAllProducts();
}
@GetMapping("/search")
public List<ProductDTO> searchProducts(
        @RequestParam String keyword) {

    return productService.searchProducts(keyword);
}
@GetMapping("/page")
public Page<ProductDTO> getProductsByPage(

        @RequestParam int page,

        @RequestParam int size) {

    return productService.getProductsByPage(page, size);
}
@GetMapping("/filter")
public Page<ProductDTO> filterProducts(

        @RequestParam String keyword,

        @RequestParam int page,

        @RequestParam int size,

        @RequestParam String sortField) {

    return productService.filterProducts(
            keyword,
            page,
            size,
            sortField);
}
@GetMapping("/low-stock")
public List<ProductDTO> getLowStockProducts() {

    return productService.getLowStockProducts();
}
@GetMapping("/expiring")
public List<ProductDTO> getExpiringProducts(
        @RequestParam LocalDate date) {

    return productService.getExpiringProducts(date);
}
@GetMapping("/sort")

public List<ProductDTO> getProductsSorted(
        @RequestParam String field) {

    return productService.getProductsSorted(field);
}
@GetMapping("/{id}")
public Optional<ProductDTO> getProductById(@PathVariable Long id) {
    return productService.getProductById(id);
} 
@PutMapping("/{id}")
public ProductDTO updateProduct(@PathVariable Long id,
                                @RequestBody ProductDTO productDTO) {

    return productService.updateProduct(id, productDTO);
}
@DeleteMapping("/{id}")
public String deleteProduct(@PathVariable Long id) {

    productService.deleteProduct(id);

    return "Product deleted successfully!";
}
}
