package com.smartshop.backend.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
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
    @NotBlank(message = "Product Name is required")
    private String productName;
    private String brand;
    private Long categoryId;
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