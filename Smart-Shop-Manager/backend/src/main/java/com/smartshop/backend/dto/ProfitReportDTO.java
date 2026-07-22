package com.smartshop.backend.dto;

import lombok.Data;

@Data
public class ProfitReportDTO {

    private Double totalSales;
    private Double totalPurchases;
    private Double totalProfit;
}