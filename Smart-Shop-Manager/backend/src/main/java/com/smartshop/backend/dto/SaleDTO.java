package com.smartshop.backend.dto;

import java.time.LocalDate;
import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SaleDTO {

    private Long id;

    @NotBlank(message = "Sale code is required")
    private String saleCode;

    @NotNull(message = "Sale date is required")
    private LocalDate saleDate;

    @NotNull(message = "Customer ID is required")
    private Long customerId;

    private Double totalAmount;

    private List<SaleItemDTO> saleItems;
}