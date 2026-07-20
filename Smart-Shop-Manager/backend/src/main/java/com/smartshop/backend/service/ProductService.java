package com.smartshop.backend.service;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.smartshop.backend.dto.ProductDTO;
import com.smartshop.backend.entity.Product;
import com.smartshop.backend.mapper.ProductMapper;
import com.smartshop.backend.repository.ProductRepository;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

   public ProductDTO saveProduct(ProductDTO productDTO) {

    Product product = ProductMapper.toEntity(productDTO);

    Product savedProduct = productRepository.save(product);

    return ProductMapper.toDTO(savedProduct);
}

public List<ProductDTO> getAllProducts() {
    List<Product> products = productRepository.findAll();
    return products.stream()
            .map(ProductMapper::toDTO)
            .toList();
}

public List<ProductDTO> searchProducts(String keyword) {

    List<Product> products = productRepository
            .findByProductNameContainingIgnoreCase(keyword);

    return products.stream()
            .map(ProductMapper::toDTO)
            .toList();
}
public Page<ProductDTO> getProductsByPage(int page, int size) {

    Page<Product> products =
            productRepository.findAll(PageRequest.of(page, size));

    return products.map(ProductMapper::toDTO);
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
        product.setCategory(updatedProduct.getCategory());
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


    
