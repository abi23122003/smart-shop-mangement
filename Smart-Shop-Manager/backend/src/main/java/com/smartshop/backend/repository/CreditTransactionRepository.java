package com.smartshop.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.smartshop.backend.entity.Credit;
import com.smartshop.backend.entity.CreditTransaction;

public interface CreditTransactionRepository extends JpaRepository<CreditTransaction, Long> {

    List<CreditTransaction> findByCredit(Credit credit);

}