package com.smartshop.backend.service;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smartshop.backend.entity.Product;
import com.smartshop.backend.repository.ProductRepository;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }
public Optional<Product> getProductById(Long id) {
    return productRepository.findById(id);
}
    public Product updateProduct(Long id, Product updatedProduct) {

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

        return productRepository.save(product);
    }

    return null;
}
public void deleteProduct(Long id) {
    productRepository.deleteById(id);
}
}


    
