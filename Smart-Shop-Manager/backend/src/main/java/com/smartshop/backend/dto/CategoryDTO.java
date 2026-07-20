package com.smartshop.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDTO {

    private Long id;

    @NotBlank(message = "Category name is required")
    private String name;

    @NotBlank(message = "Category description is required")
    private String description;

    @NotNull(message = "Active status is required")
    private Boolean active;
}