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
public class PurchaseDTO {

    private Long id;

    @NotBlank(message = "Purchase code is required")
    private String purchaseCode;

    @NotNull(message = "Purchase date is required")
    private LocalDate purchaseDate;

    @NotNull(message = "Supplier ID is required")
    private Long supplierId;
    
    private Double totalAmount;

    private List<PurchaseItemDTO> purchaseItems;
}