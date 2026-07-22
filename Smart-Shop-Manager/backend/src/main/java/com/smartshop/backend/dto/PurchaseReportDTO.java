package com.smartshop.backend.dto;

import lombok.Data;

@Data
public class PurchaseReportDTO {

    private String purchaseCode;

    private String supplierName;

    private Double totalAmount;

    private String purchaseDate;
}