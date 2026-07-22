package com.smartshop.backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.smartshop.backend.entity.Credit;
import com.smartshop.backend.entity.Customer;

public interface CreditRepository extends JpaRepository<Credit, Long> {

    Optional<Credit> findByCustomer(Customer customer);

}