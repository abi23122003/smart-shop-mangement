package com.smartshop.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.smartshop.backend.entity.SaleItem;

public interface SaleItemRepository extends JpaRepository<SaleItem, Long> {

}