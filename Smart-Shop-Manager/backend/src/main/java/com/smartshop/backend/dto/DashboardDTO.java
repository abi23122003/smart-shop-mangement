package com.smartshop.backend.dto;

import lombok.Data;

@Data
public class DashboardDTO {

    private long totalProducts;

    private long totalCategories;

    private long totalCustomers;

    private long totalSuppliers;

    private double todaySales;

    private double todayPurchases;

    private long lowStockProducts;
}