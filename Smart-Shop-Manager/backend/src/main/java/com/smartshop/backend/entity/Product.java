package com.smartshop.backend.entity;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "products")
public class Product {

@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
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