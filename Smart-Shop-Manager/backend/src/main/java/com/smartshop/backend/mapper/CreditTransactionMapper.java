package com.smartshop.backend.mapper;

import com.smartshop.backend.dto.CreditTransactionDTO;
import com.smartshop.backend.entity.CreditTransaction;

public class CreditTransactionMapper {

    public static CreditTransactionDTO toDTO(CreditTransaction transaction) {

        CreditTransactionDTO dto = new CreditTransactionDTO();

        dto.setId(transaction.getId());
        dto.setCreditId(transaction.getCredit().getId());
        dto.setTransactionDate(transaction.getTransactionDate());
        dto.setType(transaction.getType());
        dto.setAmount(transaction.getAmount());
        dto.setRemarks(transaction.getRemarks());

        return dto;
    }
}
