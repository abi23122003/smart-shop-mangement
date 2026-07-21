package com.smartshop.backend.repository;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import com.smartshop.backend.entity.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    List<Customer> findByCustomerNameContainingIgnoreCase(String keyword);

    Page<Customer> findAll(Pageable pageable);
    Page<Customer> findByCustomerNameContainingIgnoreCase(
        String keyword,
        Pageable pageable);
        @Query("SELECT COUNT(c) FROM Customer c WHERE c.active = true")
long countActiveCustomers();

@Query("SELECT COUNT(c) FROM Customer c WHERE c.active = false")
long countInactiveCustomers();
}