package com.smartshop.backend.mapper;

import com.smartshop.backend.dto.ProductDTO;
import com.smartshop.backend.entity.Product;

public class ProductMapper {

    // Convert DTO → Entity
    public static Product toEntity(ProductDTO dto) {

        Product product = new Product();

        product.setId(dto.getId());
        product.setProductCode(dto.getProductCode());
        product.setBarcode(dto.getBarcode());
        product.setProductName(dto.getProductName());
        product.setBrand(dto.getBrand());
        product.setVariant(dto.getVariant());
        product.setUnit(dto.getUnit());
        product.setQuantity(dto.getQuantity());
        product.setPurchasePrice(dto.getPurchasePrice());
        product.setSellingPrice(dto.getSellingPrice());
        product.setMinimumStock(dto.getMinimumStock());
        product.setExpiryDate(dto.getExpiryDate());
        product.setExpiryApplicable(dto.getExpiryApplicable());
        product.setActive(dto.getActive());

        return product;
    }

    // Convert Entity → DTO
  public static ProductDTO toDTO(Product product) {

    ProductDTO dto = new ProductDTO();

    dto.setId(product.getId());
    dto.setProductCode(product.getProductCode());
    dto.setBarcode(product.getBarcode());
    dto.setProductName(product.getProductName());
    dto.setBrand(product.getBrand());

    if (product.getCategory() != null) {
        dto.setCategoryId(product.getCategory().getId());
    }

    dto.setVariant(product.getVariant());
    dto.setUnit(product.getUnit());
    dto.setQuantity(product.getQuantity());
    dto.setPurchasePrice(product.getPurchasePrice());
    dto.setSellingPrice(product.getSellingPrice());
    dto.setMinimumStock(product.getMinimumStock());
    dto.setExpiryDate(product.getExpiryDate());
    dto.setExpiryApplicable(product.getExpiryApplicable());
    dto.setActive(product.getActive());

    return dto;
}
}