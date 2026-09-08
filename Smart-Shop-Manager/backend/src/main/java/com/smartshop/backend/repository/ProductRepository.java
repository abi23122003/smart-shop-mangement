package com.smartshop.backend.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

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
Optional<Product> findByBarcode(String barcode);
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
@Query("""
    SELECT p FROM Product p
    WHERE COALESCE(p.active, true) = true
      AND (:keyword = '' OR LOWER(p.productName) LIKE LOWER(CONCAT('%', :keyword, '%'))
           OR LOWER(COALESCE(p.brand, '')) LIKE LOWER(CONCAT('%', :keyword, '%'))
           OR LOWER(COALESCE(p.barcode, '')) LIKE LOWER(CONCAT('%', :keyword, '%')))
      AND (:categoryId IS NULL OR p.category.id = :categoryId)
      AND (:stockStatus = 'all'
           OR (:stockStatus = 'in' AND p.quantity > p.minimumStock)
           OR (:stockStatus = 'low' AND p.quantity > 0 AND p.quantity <= p.minimumStock)
           OR (:stockStatus = 'out' AND p.quantity = 0))
""")
Page<Product> findActiveProducts(
        @Param("keyword") String keyword,
        @Param("categoryId") Long categoryId,
        @Param("stockStatus") String stockStatus,
        Pageable pageable);

@Query("""
    SELECT p FROM Product p
    WHERE COALESCE(p.active, true) = true
      AND (:keyword = '' OR LOWER(p.productName) LIKE LOWER(CONCAT('%', :keyword, '%'))
           OR LOWER(COALESCE(p.brand, '')) LIKE LOWER(CONCAT('%', :keyword, '%'))
           OR LOWER(COALESCE(p.barcode, '')) LIKE LOWER(CONCAT('%', :keyword, '%')))
      AND (:categoryId IS NULL OR p.category.id = :categoryId)
      AND (:subcategory = '' OR LOWER(COALESCE(p.subcategory, '')) = LOWER(:subcategory))
      AND (:brand = '' OR LOWER(COALESCE(p.brand, '')) = LOWER(:brand))
      AND (:stockStatus = 'all'
           OR (:stockStatus = 'in' AND p.quantity > p.minimumStock)
           OR (:stockStatus = 'low' AND p.quantity > 0 AND p.quantity <= p.minimumStock)
           OR (:stockStatus = 'out' AND p.quantity = 0))
""")
Page<Product> findActiveProductsByClassification(
        @Param("keyword") String keyword,
        @Param("categoryId") Long categoryId,
        @Param("subcategory") String subcategory,
        @Param("brand") String brand,
        @Param("stockStatus") String stockStatus,
        Pageable pageable);
}
