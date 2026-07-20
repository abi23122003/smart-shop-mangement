package com.smartshop.backend.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.smartshop.backend.entity.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

List<Product> findByProductNameContainingIgnoreCase(String keyword);
Page<Product> findByProductNameContainingIgnoreCase(
        String keyword,
        Pageable pageable);
@Query("SELECT p FROM Product p WHERE p.quantity <= p.minimumStock")
List<Product> findLowStockProducts();
List<Product> findByExpiryDateBefore(LocalDate date);
}