package com.smartshop.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreditDTO {

    private Long id;

    private Long customerId;

    private String customerName;

    private Double totalCredit;

    private Double totalPaid;

    private Double balance;

    private String status;
}