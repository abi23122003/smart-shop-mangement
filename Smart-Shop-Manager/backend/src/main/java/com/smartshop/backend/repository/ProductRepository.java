package com.smartshop.backend.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.smartshop.backend.dto.ChartDataDTO;
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
@Query("SELECT COUNT(p) FROM Product p WHERE p.quantity <= p.minimumStock")
long countLowStockProducts();
@Query("SELECT COUNT(p) FROM Product p WHERE p.quantity = 0")
long countOutOfStockProducts();
@Query("SELECT COUNT(p) FROM Product p WHERE p.expiryDate <= :date")
long countExpiringProducts(@Param("date") LocalDate date);
@Query("SELECT SUM(p.sellingPrice * p.quantity) FROM Product p")
Double getTotalInventoryValue();
@Query("""
    SELECT new com.smartshop.backend.dto.ChartDataDTO(
        p.productName,
        CAST(p.quantity AS double)
    )
    FROM Product p
    ORDER BY p.quantity DESC
""")
List<ChartDataDTO> getStockChartData();
@Query("""
    SELECT new com.smartshop.backend.dto.ChartDataDTO(
        p.productName,
        (p.sellingPrice * p.quantity)
    )
    FROM Product p
    ORDER BY (p.sellingPrice * p.quantity) DESC
""")
List<ChartDataDTO> getInventoryValueChartData();
long countByQuantityLessThanEqual(Integer minimumStock);
@Query("""
SELECT p FROM Product p
WHERE
LOWER(p.productName) LIKE LOWER(CONCAT('%', :keyword, '%'))
OR LOWER(p.productCode) LIKE LOWER(CONCAT('%', :keyword, '%'))
OR LOWER(p.barcode) LIKE LOWER(CONCAT('%', :keyword, '%'))
""")
List<Product> searchProducts(@Param("keyword") String keyword);
}