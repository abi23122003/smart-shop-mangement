package com.smartshop.backend.dto;

import lombok.Data;

@Data
public class StockReportDTO {

    private String productCode;
    private String productName;
    private Integer quantity;
    private Integer minimumStock;
    private String stockStatus;
}