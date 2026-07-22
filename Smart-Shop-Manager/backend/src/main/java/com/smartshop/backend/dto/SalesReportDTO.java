package com.smartshop.backend.dto;

import lombok.Data;

@Data
public class SalesReportDTO {

    private String saleCode;

    private String customerName;

    private Double totalAmount;

    private String saleDate;
}