package com.smartshop.backend.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import com.smartshop.backend.mapper.CreditTransactionMapper;
import com.smartshop.backend.entity.Credit;
import com.smartshop.backend.entity.CreditTransaction;
import org.springframework.stereotype.Service;
import com.smartshop.backend.dto.CreditDTO;
import com.smartshop.backend.dto.CreditTransactionDTO;
import com.smartshop.backend.service.CreditService;
import com.smartshop.backend.entity.Customer;
import com.smartshop.backend.mapper.CreditMapper;
import com.smartshop.backend.repository.CreditRepository;
import com.smartshop.backend.repository.CreditTransactionRepository;
import com.smartshop.backend.repository.CustomerRepository;

@Service
public class CreditServiceImpl implements CreditService {
    private final CreditRepository creditRepository;
    private final CreditTransactionRepository creditTransactionRepository;
        private final CustomerRepository customerRepository;


    public CreditServiceImpl(CreditRepository creditRepository, CreditTransactionRepository creditTransactionRepository, CustomerRepository customerRepository) {
        this.creditRepository = creditRepository;
        this.creditTransactionRepository = creditTransactionRepository;
        this.customerRepository = customerRepository;
    }

@Override
public CreditDTO addCreditPurchase(Long customerId, Double amount, String remarks) {

    Customer customer = customerRepository.findById(customerId)
            .orElseThrow(() -> new RuntimeException("Customer not found"));

    Optional<Credit> optionalCredit = creditRepository.findByCustomer(customer);

    Credit credit;

   if (optionalCredit.isPresent()) {

    credit = optionalCredit.get();

    Double totalCredit = credit.getTotalCredit() == null ? 0.0 : credit.getTotalCredit();
    Double balance = credit.getBalance() == null ? 0.0 : credit.getBalance();

    credit.setTotalCredit(totalCredit + amount);
    credit.setBalance(balance + amount);

} else {

    credit = new Credit();

    credit.setCustomer(customer);
    credit.setTotalCredit(amount);
    credit.setTotalPaid(0.0);
    credit.setBalance(amount);
    credit.setStatus("PENDING");
}

    creditRepository.save(credit);
    CreditTransaction transaction = new CreditTransaction();

    transaction.setCredit(credit);
    transaction.setTransactionDate(java.time.LocalDate.now());
    transaction.setType("PURCHASE");
    transaction.setAmount(amount);
    transaction.setRemarks(remarks);

creditTransactionRepository.save(transaction);

return CreditMapper.toDTO(credit);


}
 @Override
public CreditDTO recordPayment(Long customerId, Double amount, String remarks) {

    Customer customer = customerRepository.findById(customerId)
            .orElseThrow(() -> new RuntimeException("Customer not found"));

    Credit credit = creditRepository.findByCustomer(customer)
            .orElseThrow(() -> new RuntimeException("No credit account found"));

    credit.setTotalPaid(credit.getTotalPaid() + amount);
    credit.setBalance(credit.getBalance() - amount);

    if (credit.getBalance() <= 0) {
        credit.setBalance(0.0);
        credit.setStatus("PAID");
    } else {
        credit.setStatus("PENDING");
    }

    creditRepository.save(credit);

    CreditTransaction transaction = new CreditTransaction();
    transaction.setCredit(credit);
    transaction.setTransactionDate(java.time.LocalDate.now());
    transaction.setType("PAYMENT");
    transaction.setAmount(amount);
    transaction.setRemarks(remarks);

    creditTransactionRepository.save(transaction);

    return CreditMapper.toDTO(credit);
}

   @Override
public CreditDTO getCreditByCustomer(Long customerId) {

    Customer customer = customerRepository.findById(customerId)
            .orElseThrow(() -> new RuntimeException("Customer not found"));

    Credit credit = creditRepository.findByCustomer(customer)
            .orElseThrow(() -> new RuntimeException("No credit account found"));

    return CreditMapper.toDTO(credit);
}
    @Override
public List<CreditDTO> getAllCredits() {

    return creditRepository.findAll()
            .stream()
            .map(CreditMapper::toDTO)
            .collect(Collectors.toList());
}
@Override
public List<CreditTransactionDTO> getTransactionHistory(Long customerId) {

    Customer customer = customerRepository.findById(customerId)
            .orElseThrow(() -> new RuntimeException("Customer not found"));

    Credit credit = creditRepository.findByCustomer(customer)
            .orElseThrow(() -> new RuntimeException("No credit account found"));

    return creditTransactionRepository.findByCredit(credit)
            .stream()
            .map(CreditTransactionMapper::toDTO)
            .collect(Collectors.toList());
}
    
}
