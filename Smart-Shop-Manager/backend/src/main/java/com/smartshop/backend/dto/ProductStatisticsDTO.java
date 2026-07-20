package com.smartshop.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductStatisticsDTO {

    private long totalProducts;

    private long lowStockProducts;

    private long outOfStockProducts;

    private long expiringProducts;

    private double inventoryValue;

}