package com.smartshop.backend.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.smartshop.backend.entity.Supplier;

@Repository
public interface SupplierRepository extends JpaRepository<Supplier, Long> {

    List<Supplier> findBySupplierNameContainingIgnoreCase(String keyword);
Page<Supplier> findBySupplierNameContainingIgnoreCase(
        String keyword,
        Pageable pageable);
        @Query("SELECT COUNT(s) FROM Supplier s WHERE s.active = true")
long countActiveSuppliers();

@Query("SELECT COUNT(s) FROM Supplier s WHERE s.active = false")
long countInactiveSuppliers();
}