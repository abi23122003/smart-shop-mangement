package com.smartshop.backend.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.smartshop.backend.dto.SaleDTO;
import com.smartshop.backend.service.SaleService;

@RestController
@RequestMapping("/api/sales")
@CrossOrigin(origins = "*")
public class SaleController {

    private final SaleService saleService;

    public SaleController(SaleService saleService) {
        this.saleService = saleService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SaleDTO createSale(@RequestBody SaleDTO saleDTO) {
        return saleService.saveSale(saleDTO);
    }

    @GetMapping
    public List<SaleDTO> getAllSales() {
        return saleService.getAllSales();
    }

    @GetMapping("/{id}")
    public SaleDTO getSaleById(@PathVariable Long id) {
        return saleService.getSaleById(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteSale(@PathVariable Long id) {
        saleService.deleteSale(id);
    }

    @PutMapping("/{id}")
    public SaleDTO updateSale(
            @PathVariable Long id,
            @RequestBody SaleDTO saleDTO) {

        return saleService.updateSale(id, saleDTO);
    }
}