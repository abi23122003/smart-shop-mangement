package com.smartshop.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.smartshop.backend.entity.Sale;

public interface SaleRepository extends JpaRepository<Sale, Long> {

}