package com.smartshop.backend.controller;
import java.util.List;
import com.smartshop.backend.service.CreditService;
import org.springframework.web.bind.annotation.*;
import com.smartshop.backend.dto.CreditDTO;
import com.smartshop.backend.dto.CreditRequest;
import com.smartshop.backend.dto.CreditTransactionDTO;

@RestController
@RequestMapping("/api/credits")
@CrossOrigin(origins = "*")
public class CreditController {

    private final CreditService creditService;

    public CreditController(CreditService creditService) {
        this.creditService = creditService;
    }
    @PostMapping("/purchase")
public CreditDTO addCreditPurchase(@RequestBody CreditRequest request) {

    return creditService.addCreditPurchase(
            request.getCustomerId(),
            request.getAmount(),
            request.getRemarks()
    );
}
@PostMapping("/payment")
public CreditDTO recordPayment(@RequestBody CreditRequest request) {

    return creditService.recordPayment(
            request.getCustomerId(),
            request.getAmount(),
            request.getRemarks()
    );
}
@GetMapping("/customer/{customerId}")
public CreditDTO getCreditByCustomer(@PathVariable Long customerId) {

    return creditService.getCreditByCustomer(customerId);
}
@GetMapping
public List<CreditDTO> getAllCredits() {

    return creditService.getAllCredits();
}
@GetMapping("/customer/{customerId}/history")
public List<CreditTransactionDTO> getTransactionHistory(@PathVariable Long customerId) {

    return creditService.getTransactionHistory(customerId);
}
}