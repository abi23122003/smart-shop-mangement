package com.smartshop.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SupplierStatisticsDTO {

    private long totalSuppliers;
    private long activeSuppliers;
    private long inactiveSuppliers;
}