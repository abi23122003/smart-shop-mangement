package com.smartshop.backend.dto;

import lombok.Data;

@Data
public class ProductReportDTO {

    private String productCode;
    private String productName;
    private String categoryName;
    private String brand;
    private Integer quantity;
    private Double purchasePrice;
    private Double sellingPrice;
}