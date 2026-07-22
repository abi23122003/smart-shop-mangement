package com.smartshop.backend.mapper;

import com.smartshop.backend.dto.CreditDTO;
import com.smartshop.backend.entity.Credit;

public class CreditMapper {

    public static CreditDTO toDTO(Credit credit) {

        CreditDTO dto = new CreditDTO();

        dto.setId(credit.getId());
        dto.setCustomerId(credit.getCustomer().getId());
        dto.setCustomerName(credit.getCustomer().getCustomerName()); // or getName(), depending on your Customer entity
        dto.setTotalCredit(credit.getTotalCredit());
        dto.setTotalPaid(credit.getTotalPaid());
        dto.setBalance(credit.getBalance());
        dto.setStatus(credit.getStatus());

        return dto;
    }
}