package com.smartshop.backend.entity;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "purchases")
public class Purchase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String purchaseCode;

    private LocalDate purchaseDate;

    @ManyToOne
    @JoinColumn(name = "supplier_id")
    private Supplier supplier;

    private Double totalAmount;

    @OneToMany(mappedBy = "purchase",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<PurchaseItem> purchaseItems;
}