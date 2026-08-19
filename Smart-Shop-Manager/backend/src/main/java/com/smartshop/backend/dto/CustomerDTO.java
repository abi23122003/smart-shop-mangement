package com.smartshop.backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDTO {

    private Long id;

    private String customerCode;

    @NotBlank(message = "Customer name is required")
    private String customerName;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "\\d{10}", message = "Phone number must contain exactly 10 digits")
    private String phone;

    @Email(message = "Invalid email format")
    private String email;

    private String address;

    @NotNull(message = "Credit limit is required")
    private Double creditLimit;

    private Boolean creditEnabled;

    private Boolean active;
}