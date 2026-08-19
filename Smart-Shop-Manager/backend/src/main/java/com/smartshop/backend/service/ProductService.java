package com.smartshop.backend.service;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import com.smartshop.backend.entity.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import com.smartshop.backend.repository.CategoryRepository;
import com.smartshop.backend.dto.ChartDataDTO;
import com.smartshop.backend.dto.ProductDTO;
import com.smartshop.backend.dto.ProductRestockRequest;
import com.smartshop.backend.dto.ProductStatisticsDTO;
import com.smartshop.backend.entity.Product;
import com.smartshop.backend.mapper.ProductMapper;
import com.smartshop.backend.repository.ProductRepository;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CategoryRepository categoryRepository;

 public ProductDTO saveProduct(ProductDTO productDTO) {

    Product product = ProductMapper.toEntity(productDTO);

    Category category = categoryRepository.findById(productDTO.getCategoryId())
            .orElseThrow(() -> new RuntimeException("Category not found"));

    product.setCategory(category);

    Product savedProduct = productRepository.save(product);

    return ProductMapper.toDTO(savedProduct);
}

public List<ProductDTO> getAllProducts() {
    List<Product> products = productRepository.findAll();
    return products.stream()
            .map(ProductMapper::toDTO)
            .toList();
}

public ProductDTO restockProduct(ProductRestockRequest request) {
    String barcode = request.getBarcode().trim();
    Product product = productRepository.findByBarcode(barcode)
            .orElseThrow(() -> new RuntimeException("No product found for barcode: " + barcode));

    int currentQuantity = product.getQuantity() == null ? 0 : product.getQuantity();
    product.setQuantity(currentQuantity + request.getQuantity());

    return ProductMapper.toDTO(productRepository.save(product));
}

public List<ProductDTO> searchProducts(String keyword) {

    List<Product> products = productRepository.searchProducts(keyword);

    return products.stream()
            .map(ProductMapper::toDTO)
            .toList();
}
public Page<ProductDTO> getProductsByPage(int page, int size) {

    Page<Product> products =
            productRepository.findAll(PageRequest.of(page, size));

    return products.map(ProductMapper::toDTO);
}
public List<ProductDTO> getProductsSorted(String field, String direction) {

    Sort sort = direction.equalsIgnoreCase("desc")
            ? Sort.by(field).descending()
            : Sort.by(field).ascending();

    List<Product> products = productRepository.findAll(sort);

    return products.stream()
            .map(ProductMapper::toDTO)
            .toList();
}
public Page<ProductDTO> filterProducts(
        String keyword,
        int page,
        int size,
        String sortField) {

    Pageable pageable = PageRequest.of(
            page,
            size,
            Sort.by(sortField));

    Page<Product> products =
            productRepository.findByProductNameContainingIgnoreCase(
                    keyword,
                    pageable);

    return products.map(ProductMapper::toDTO);
}
public List<ProductDTO> getLowStockProducts() {

    List<Product> products = productRepository.findLowStockProducts();

    return products.stream()
            .map(ProductMapper::toDTO)
            .toList();
}
public List<ProductDTO> getExpiringProducts(LocalDate date) {

    List<Product> products = productRepository.findByExpiryDateBefore(date);

    return products.stream()
            .map(ProductMapper::toDTO)
            .toList();
}
public ProductStatisticsDTO getProductStatistics() {

    long totalProducts = productRepository.count();

    long lowStockProducts = productRepository.countLowStockProducts();

    long outOfStockProducts = productRepository.countOutOfStockProducts();

    long expiringProducts = productRepository.countExpiringProducts(LocalDate.now());

    Double inventoryValue = productRepository.getTotalInventoryValue();

    if (inventoryValue == null) {
        inventoryValue = 0.0;
    }

    return new ProductStatisticsDTO(
            totalProducts,
            lowStockProducts,
            outOfStockProducts,
            expiringProducts,
            inventoryValue
    );
}
public List<ChartDataDTO> getStockChartData() {

    return productRepository.getStockChartData();
}
public List<ChartDataDTO> getInventoryValueChartData() {

    return productRepository.getInventoryValueChartData();
}
public Optional<ProductDTO> getProductById(Long id) {

    Optional<Product> product = productRepository.findById(id);

    return product.map(ProductMapper::toDTO);
}
    public ProductDTO updateProduct(Long id, ProductDTO updatedProduct){

    Optional<Product> existingProduct = productRepository.findById(id);

    if (existingProduct.isPresent()) {

        Product product = existingProduct.get();

        product.setProductCode(updatedProduct.getProductCode());
        product.setBarcode(updatedProduct.getBarcode());
        product.setProductName(updatedProduct.getProductName());
        product.setBrand(updatedProduct.getBrand());
        Category category = categoryRepository.findById(updatedProduct.getCategoryId())
        .orElseThrow(() -> new RuntimeException("Category not found"));

product.setCategory(category);
        product.setVariant(updatedProduct.getVariant());
        product.setUnit(updatedProduct.getUnit());
        product.setQuantity(updatedProduct.getQuantity());
        product.setPurchasePrice(updatedProduct.getPurchasePrice());
        product.setSellingPrice(updatedProduct.getSellingPrice());
        product.setMinimumStock(updatedProduct.getMinimumStock());
        product.setExpiryDate(updatedProduct.getExpiryDate());
        product.setExpiryApplicable(updatedProduct.getExpiryApplicable());
        product.setActive(updatedProduct.getActive());

        Product savedProduct = productRepository.save(product);

        return ProductMapper.toDTO(savedProduct);
    }

    return null;
}
public void deleteProduct(Long id) {
    productRepository.deleteById(id);
}

}


    
