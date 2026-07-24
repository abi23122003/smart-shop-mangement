package com.smartshop.backend.controller;

import java.io.ByteArrayInputStream;
import java.util.List;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import com.smartshop.backend.dto.SaleDTO;
import com.smartshop.backend.pdf.InvoiceService;
import com.smartshop.backend.service.SaleService;

@RestController
@RequestMapping("/api/sales")
public class SaleController {

    private final SaleService saleService;
    private final InvoiceService invoiceService;

    public SaleController(
            SaleService saleService,
            InvoiceService invoiceService) {

        this.saleService = saleService;
        this.invoiceService = invoiceService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SaleDTO createSale(@Valid @RequestBody SaleDTO saleDTO) {
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

    @PutMapping("/{id}")
    public SaleDTO updateSale(
            @PathVariable Long id,
            @RequestBody SaleDTO saleDTO) {

        return saleService.updateSale(id, saleDTO);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteSale(@PathVariable Long id) {
        saleService.deleteSale(id);
    }

    @GetMapping("/{id}/invoice")
    public ResponseEntity<InputStreamResource> downloadInvoice(
            @PathVariable Long id) {

        ByteArrayInputStream pdf = invoiceService.generateInvoice(id);

        HttpHeaders headers = new HttpHeaders();
        headers.add(
                HttpHeaders.CONTENT_DISPOSITION,
                "inline; filename=invoice-" + id + ".pdf");

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(pdf));
    }
}
