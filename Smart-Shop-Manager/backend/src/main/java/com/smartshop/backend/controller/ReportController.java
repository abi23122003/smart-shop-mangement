package com.smartshop.backend.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.smartshop.backend.dto.PurchaseReportDTO;
import com.smartshop.backend.dto.SalesReportDTO;
import com.smartshop.backend.dto.ProductReportDTO;
import com.smartshop.backend.dto.StockReportDTO;
import com.smartshop.backend.service.ReportService;
import com.smartshop.backend.dto.CustomerReportDTO;
import com.smartshop.backend.dto.SupplierReportDTO;
import com.smartshop.backend.dto.ProfitReportDTO;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @GetMapping("/sales")
    public List<SalesReportDTO> getSalesReport() {
        return reportService.getSalesReport();
    }
    @GetMapping("/purchases")
public List<PurchaseReportDTO> getPurchaseReport() {
    return reportService.getPurchaseReport();
}
@GetMapping("/products")
public List<ProductReportDTO> getProductReport() {
    return reportService.getProductReport();
}
@GetMapping("/stock")
public List<StockReportDTO> getStockReport() {
    return reportService.getStockReport();
}
@GetMapping("/customers")
public List<CustomerReportDTO> getCustomerReport() {
    return reportService.getCustomerReport();
}
@GetMapping("/suppliers")
public List<SupplierReportDTO> getSupplierReport() {
    return reportService.getSupplierReport();
}
@GetMapping("/profit")
public ProfitReportDTO getProfitReport() {
    return reportService.getProfitReport();
}
}
