package com.smartshop.backend.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.smartshop.backend.dto.PurchaseDTO;
import com.smartshop.backend.service.PurchaseService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/purchases")
@Validated
public class PurchaseController {

    private final PurchaseService purchaseService;

    public PurchaseController(PurchaseService purchaseService) {
        this.purchaseService = purchaseService;
    }

    // Create Purchase
    @PostMapping
    public ResponseEntity<PurchaseDTO> savePurchase(
            @Valid @RequestBody PurchaseDTO purchaseDTO) {

        PurchaseDTO savedPurchase = purchaseService.savePurchase(purchaseDTO);

        return new ResponseEntity<>(savedPurchase, HttpStatus.CREATED);
    }

    // Get All Purchases
    @GetMapping
    public ResponseEntity<List<PurchaseDTO>> getAllPurchases() {

        return ResponseEntity.ok(purchaseService.getAllPurchases());
    }
}