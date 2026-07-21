package com.smartshop.backend.mapper;

import com.smartshop.backend.dto.SupplierDTO;
import com.smartshop.backend.entity.Supplier;

public class SupplierMapper {

    public static Supplier toEntity(SupplierDTO dto) {

        Supplier supplier = new Supplier();

        supplier.setId(dto.getId());
        supplier.setSupplierCode(dto.getSupplierCode());
        supplier.setSupplierName(dto.getSupplierName());
        supplier.setContactPerson(dto.getContactPerson());
        supplier.setPhone(dto.getPhone());
        supplier.setEmail(dto.getEmail());
        supplier.setAddress(dto.getAddress());
        supplier.setGstNumber(dto.getGstNumber());
        supplier.setActive(dto.getActive());

        return supplier;
    }

   
    public static SupplierDTO toDTO(Supplier supplier) {

        SupplierDTO dto = new SupplierDTO();

        dto.setId(supplier.getId());
        dto.setSupplierCode(supplier.getSupplierCode());
        dto.setSupplierName(supplier.getSupplierName());
        dto.setContactPerson(supplier.getContactPerson());
        dto.setPhone(supplier.getPhone());
        dto.setEmail(supplier.getEmail());
        dto.setAddress(supplier.getAddress());
        dto.setGstNumber(supplier.getGstNumber());
        dto.setActive(supplier.getActive());

        return dto;
    }
}