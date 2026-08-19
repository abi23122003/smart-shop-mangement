package com.smartshop.backend.mapper;

import com.smartshop.backend.dto.CustomerDTO;
import com.smartshop.backend.entity.Customer;

public class CustomerMapper {

    // DTO → Entity
    public static Customer toEntity(CustomerDTO dto) {

        Customer customer = new Customer();

        customer.setId(dto.getId());
        customer.setCustomerCode(dto.getCustomerCode());
        customer.setCustomerName(dto.getCustomerName());
        customer.setPhone(dto.getPhone());
        customer.setEmail(dto.getEmail());
        customer.setAddress(dto.getAddress());
        customer.setCreditLimit(dto.getCreditLimit());
        customer.setCreditEnabled(dto.getCreditEnabled());
        customer.setActive(dto.getActive());

        return customer;
    }

    // Entity → DTO
    public static CustomerDTO toDTO(Customer customer) {

        CustomerDTO dto = new CustomerDTO();

        dto.setId(customer.getId());
        dto.setCustomerCode(customer.getCustomerCode());
        dto.setCustomerName(customer.getCustomerName());
        dto.setPhone(customer.getPhone());
        dto.setEmail(customer.getEmail());
        dto.setAddress(customer.getAddress());
        dto.setCreditLimit(customer.getCreditLimit());
        dto.setCreditEnabled(customer.getCreditEnabled());
        dto.setActive(customer.getActive());

        return dto;
    }
}