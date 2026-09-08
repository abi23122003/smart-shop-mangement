package com.smartshop.backend.entity;
import java.time.LocalDate;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "credit_transactions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreditTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "credit_id")
    private Credit credit;

    @ManyToOne
    @JoinColumn(name = "sale_id")
    private Sale sale;

    private LocalDate transactionDate;

    private String type;

    private Double amount;

    private String remarks;
}
