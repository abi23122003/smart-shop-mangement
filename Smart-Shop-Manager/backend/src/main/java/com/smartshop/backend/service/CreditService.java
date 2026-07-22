package com.smartshop.backend.service;

import java.util.List;

import com.smartshop.backend.dto.CreditDTO;
import com.smartshop.backend.dto.CreditTransactionDTO;

public interface CreditService {

    CreditDTO addCreditPurchase(Long customerId, Double amount, String remarks);

    CreditDTO recordPayment(Long customerId, Double amount, String remarks);

    CreditDTO getCreditByCustomer(Long customerId);

    List<CreditDTO> getAllCredits();

    List<CreditTransactionDTO> getTransactionHistory(Long customerId);
}
