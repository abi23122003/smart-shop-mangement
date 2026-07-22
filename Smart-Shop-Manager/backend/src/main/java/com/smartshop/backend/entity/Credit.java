package com.smartshop.backend.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "credits")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Credit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "customer_id", unique = true)
    private Customer customer;

    private Double totalCredit;

    private Double totalPaid;

    private Double balance;

    private String status;
}