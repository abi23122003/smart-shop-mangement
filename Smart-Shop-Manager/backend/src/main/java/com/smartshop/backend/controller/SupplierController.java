package com.smartshop.backend.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.smartshop.backend.dto.SupplierDTO;
import com.smartshop.backend.dto.SupplierStatisticsDTO;
import com.smartshop.backend.service.SupplierService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/suppliers")
public class SupplierController {

    @Autowired
    private SupplierService supplierService;

    // Create Supplier
    @PostMapping
    public SupplierDTO saveSupplier(@Valid @RequestBody SupplierDTO supplierDTO) {

        return supplierService.saveSupplier(supplierDTO);
    }

    // Get All Suppliers
    @GetMapping
    public List<SupplierDTO> getAllSuppliers() {

        return supplierService.getAllSuppliers();
    }
    @GetMapping("/search")
public List<SupplierDTO> searchSuppliers(
        @RequestParam String keyword) {

    return supplierService.searchSuppliers(keyword);
}
@GetMapping("/page")
public Page<SupplierDTO> getSuppliersByPage(
        @RequestParam int page,
        @RequestParam int size) {

    return supplierService.getSuppliersByPage(page, size);
}
@GetMapping("/sort")
public List<SupplierDTO> getSuppliersSorted(
        @RequestParam String field) {

    return supplierService.getSuppliersSorted(field);
}
@GetMapping("/filter")
public Page<SupplierDTO> filterSuppliers(
        @RequestParam String keyword,
        @RequestParam int page,
        @RequestParam int size,
        @RequestParam String sortField) {

    return supplierService.filterSuppliers(
            keyword,
            page,
            size,
            sortField);
}
@GetMapping("/statistics")
public SupplierStatisticsDTO getSupplierStatistics() {

    return supplierService.getSupplierStatistics();
}
    @GetMapping("/{id}")
public Optional<SupplierDTO> getSupplierById(@PathVariable Long id) {

    return supplierService.getSupplierById(id);
}
@PutMapping("/{id}")
public SupplierDTO updateSupplier(
        @PathVariable Long id,
        @Valid @RequestBody SupplierDTO supplierDTO) {

    return supplierService.updateSupplier(id, supplierDTO);
}
@DeleteMapping("/{id}")
public String deleteSupplier(@PathVariable Long id) {

    supplierService.deleteSupplier(id);

    return "Supplier deleted successfully!";
}
}
