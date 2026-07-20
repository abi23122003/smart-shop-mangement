package com.smartshop.backend.mapper;

import com.smartshop.backend.dto.CategoryDTO;
import com.smartshop.backend.entity.Category;

public class CategoryMapper {

    public static Category toEntity(CategoryDTO dto) {

        if (dto == null) {
            return null;
        }

        Category category = new Category();

        category.setId(dto.getId());
        category.setName(dto.getName());
        category.setDescription(dto.getDescription());
        category.setActive(dto.getActive());

        return category;
    }

    public static CategoryDTO toDTO(Category category) {

        if (category == null) {
            return null;
        }

        CategoryDTO dto = new CategoryDTO();

        dto.setId(category.getId());
        dto.setName(category.getName());
        dto.setDescription(category.getDescription());
        dto.setActive(category.getActive());

        return dto;
    }
}