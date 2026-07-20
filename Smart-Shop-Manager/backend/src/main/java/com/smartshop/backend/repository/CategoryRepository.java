package com.smartshop.backend.repository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.smartshop.backend.entity.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    List<Category> findByNameContainingIgnoreCase(String keyword);
    Page<Category> findByNameContainingIgnoreCase(
        String keyword,
        Pageable pageable);
@Query("SELECT COUNT(c) FROM Category c WHERE c.active = true")
long countActiveCategories();

@Query("SELECT COUNT(c) FROM Category c WHERE c.active = false")
long countInactiveCategories();
}