package com.smartshop.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryStatisticsDTO {

    private long totalCategories;

    private long activeCategories;

    private long inactiveCategories;
}