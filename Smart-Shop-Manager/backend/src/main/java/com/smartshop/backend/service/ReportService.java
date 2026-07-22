package com.smartshop.backend.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.smartshop.backend.dto.SalesReportDTO;
import com.smartshop.backend.entity.Sale;
import com.smartshop.backend.repository.SaleRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final SaleRepository saleRepository;

    public List<SalesReportDTO> getSalesReport() {

    return saleRepository.findAll()
            .stream()
            .map(this::convertToDTO)
            .toList();
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