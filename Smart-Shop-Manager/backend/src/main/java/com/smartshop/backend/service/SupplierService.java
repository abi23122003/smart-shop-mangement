package com.smartshop.backend.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.smartshop.backend.dto.SupplierDTO;
import com.smartshop.backend.dto.SupplierStatisticsDTO;
import com.smartshop.backend.entity.Supplier;
import com.smartshop.backend.mapper.SupplierMapper;
import com.smartshop.backend.repository.SupplierRepository;

@Service
public class SupplierService {

    @Autowired
    private SupplierRepository supplierRepository;

    // Save Supplier
    public SupplierDTO saveSupplier(SupplierDTO supplierDTO) {

        Supplier supplier = SupplierMapper.toEntity(supplierDTO);

        Supplier savedSupplier = supplierRepository.save(supplier);

        return SupplierMapper.toDTO(savedSupplier);
    }

    // Get All Suppliers
    public List<SupplierDTO> getAllSuppliers() {

        List<Supplier> suppliers = supplierRepository.findAll();

        return suppliers.stream()
                .map(SupplierMapper::toDTO)
                .toList();
    }
    public Optional<SupplierDTO> getSupplierById(Long id) {

    Optional<Supplier> supplier = supplierRepository.findById(id);

    return supplier.map(SupplierMapper::toDTO);
}
public List<SupplierDTO> searchSuppliers(String keyword) {

    List<Supplier> suppliers =
            supplierRepository.findBySupplierNameContainingIgnoreCase(keyword);

    return suppliers.stream()
            .map(SupplierMapper::toDTO)
            .toList();
}
public Page<SupplierDTO> getSuppliersByPage(int page, int size) {

    Page<Supplier> suppliers =
            supplierRepository.findAll(PageRequest.of(page, size));

    return suppliers.map(SupplierMapper::toDTO);
}
public List<SupplierDTO> getSuppliersSorted(String field) {

    List<Supplier> suppliers =
            supplierRepository.findAll(Sort.by(field));

    return suppliers.stream()
            .map(SupplierMapper::toDTO)
            .toList();
}
public Page<SupplierDTO> filterSuppliers(
        String keyword,
        int page,
        int size,
        String sortField) {

    Pageable pageable = PageRequest.of(
            page,
            size,
            Sort.by(sortField));

    Page<Supplier> suppliers =
            supplierRepository.findBySupplierNameContainingIgnoreCase(
                    keyword,
                    pageable);

    return suppliers.map(SupplierMapper::toDTO);
}
public SupplierDTO updateSupplier(Long id, SupplierDTO updatedSupplier) {

    Optional<Supplier> existingSupplier = supplierRepository.findById(id);

    if (existingSupplier.isPresent()) {

        Supplier supplier = existingSupplier.get();

        supplier.setSupplierCode(updatedSupplier.getSupplierCode());
        supplier.setSupplierName(updatedSupplier.getSupplierName());
        supplier.setContactPerson(updatedSupplier.getContactPerson());
        supplier.setPhone(updatedSupplier.getPhone());
        supplier.setEmail(updatedSupplier.getEmail());
        supplier.setAddress(updatedSupplier.getAddress());
        supplier.setGstNumber(updatedSupplier.getGstNumber());
        supplier.setActive(updatedSupplier.getActive());

        Supplier savedSupplier = supplierRepository.save(supplier);

        return SupplierMapper.toDTO(savedSupplier);
    }

    return null;
}
public void deleteSupplier(Long id) {

    supplierRepository.deleteById(id);
}
public SupplierStatisticsDTO getSupplierStatistics() {

    long totalSuppliers = supplierRepository.count();

    long activeSuppliers = supplierRepository.countActiveSuppliers();

    long inactiveSuppliers = supplierRepository.countInactiveSuppliers();

    return new SupplierStatisticsDTO(
            totalSuppliers,
            activeSuppliers,
            inactiveSuppliers
    );
}
}