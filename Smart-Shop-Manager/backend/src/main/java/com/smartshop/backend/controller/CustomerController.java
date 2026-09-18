package com.smartshop.backend.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.smartshop.backend.dto.CustomerDTO;
import com.smartshop.backend.dto.CustomerStatisticsDTO;
import com.smartshop.backend.service.CustomerService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/customers")
@Validated
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    // Create Customer
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CustomerDTO> saveCustomer(
            @Valid @RequestBody CustomerDTO customerDTO) {

        CustomerDTO savedCustomer = customerService.saveCustomer(customerDTO);

        return new ResponseEntity<>(savedCustomer, HttpStatus.CREATED);
    }

    // Get All Customers
    @GetMapping
    public ResponseEntity<List<CustomerDTO>> getAllCustomers() {

        return ResponseEntity.ok(customerService.getAllCustomers());
    }

    // Get Customer By ID
    @GetMapping("/{id}")
    public ResponseEntity<CustomerDTO> getCustomerById(@PathVariable Long id) {

        return ResponseEntity.ok(customerService.getCustomerById(id));
    }

    // Update Customer
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CustomerDTO> updateCustomer(
            @PathVariable Long id,
            @Valid @RequestBody CustomerDTO customerDTO) {

        return ResponseEntity.ok(customerService.updateCustomer(id, customerDTO));
    }

    // Delete Customer
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteCustomer(@PathVariable Long id) {

        return ResponseEntity.ok(customerService.deleteCustomer(id));
    }

    // Search Customers
    @GetMapping("/search")
    public ResponseEntity<List<CustomerDTO>> searchCustomers(
            @RequestParam String keyword) {

        return ResponseEntity.ok(customerService.searchCustomers(keyword));
    }

    // Get Customers with Pagination
    @GetMapping("/page")
    public ResponseEntity<Page<CustomerDTO>> getCustomersByPage(
            @RequestParam int page,
            @RequestParam int size) {

        return ResponseEntity.ok(
                customerService.getCustomersByPage(page, size));
    }

    // Get Customers with Sorting
    @GetMapping("/sort")
    public ResponseEntity<List<CustomerDTO>> getCustomersSorted(
            @RequestParam String field) {

        return ResponseEntity.ok(
                customerService.getCustomersSorted(field));
    }

    // Search + Pagination + Sorting
    @GetMapping("/filter")
    public ResponseEntity<Page<CustomerDTO>> filterCustomers(
            @RequestParam String keyword,
            @RequestParam int page,
            @RequestParam int size,
            @RequestParam String sortField) {

        return ResponseEntity.ok(
                customerService.filterCustomers(
                        keyword,
                        page,
                        size,
                        sortField));
    }

    @GetMapping("/statistics")
    public ResponseEntity<CustomerStatisticsDTO> getCustomerStatistics() {

        return ResponseEntity.ok(
                customerService.getCustomerStatistics());
    }
}