package com.smartshop.backend.service;

import java.util.List;

import org.springframework.stereotype.Service;
import com.smartshop.backend.dto.SalesReportDTO;
import com.smartshop.backend.entity.Sale;
import com.smartshop.backend.repository.SaleRepository;

import lombok.RequiredArgsConstructor;
import com.smartshop.backend.dto.PurchaseReportDTO;
import com.smartshop.backend.entity.Purchase;
import com.smartshop.backend.repository.PurchaseRepository;
@Service
@RequiredArgsConstructor
public class ReportService {

    private final SaleRepository saleRepository;
    private final PurchaseRepository purchaseRepository;

    public List<SalesReportDTO> getSalesReport() {

    return saleRepository.findAll()
            .stream()
            .map(this::convertToDTO)
            .toList();
}
public List<PurchaseReportDTO> getPurchaseReport() {

    return purchaseRepository.findAll()
            .stream()
            .map(this::convertPurchaseToDTO)
            .toList();
}
private PurchaseReportDTO convertPurchaseToDTO(Purchase purchase) {

    PurchaseReportDTO dto = new PurchaseReportDTO();

    dto.setPurchaseCode(purchase.getPurchaseCode());

    if (purchase.getSupplier() != null) {
        dto.setSupplierName(purchase.getSupplier().getSupplierName());
    } else {
        dto.setSupplierName("No Supplier");
    }

    dto.setPurchaseDate(purchase.getPurchaseDate().toString());
    dto.setTotalAmount(purchase.getTotalAmount());

    return dto;
}
private SalesReportDTO convertToDTO(Sale sale) {

    SalesReportDTO dto = new SalesReportDTO();

    dto.setSaleCode(sale.getSaleCode());
    dto.setCustomerName(sale.getCustomer().getCustomerName());
    dto.setSaleDate(sale.getSaleDate().toString());
    dto.setTotalAmount(sale.getTotalAmount());

    return dto;
}

}