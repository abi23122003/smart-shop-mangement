package com.smartshop.backend.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {

    private Long id;
    private String productCode;
    private String barcode;
    private String productName;
    private String brand;
    private String category;
    private String variant;
    private String unit;
    private Integer quantity;
    private Double purchasePrice;
    private Double sellingPrice;
    private Integer minimumStock;
    private LocalDate expiryDate;
    private Boolean expiryApplicable;
    private Boolean active;
}