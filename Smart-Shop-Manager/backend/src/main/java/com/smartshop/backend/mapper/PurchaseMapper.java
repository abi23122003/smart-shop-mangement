package com.smartshop.backend.mapper;

import com.smartshop.backend.dto.PurchaseDTO;
import com.smartshop.backend.entity.Purchase;
import java.util.List;
import java.util.stream.Collectors;

import com.smartshop.backend.dto.PurchaseItemDTO;
import com.smartshop.backend.entity.PurchaseItem;

public class PurchaseMapper {

    // DTO -> Entity
    public static Purchase toEntity(PurchaseDTO dto) {

        Purchase purchase = new Purchase();

        purchase.setId(dto.getId());
        purchase.setPurchaseCode(dto.getPurchaseCode());
        purchase.setPurchaseDate(dto.getPurchaseDate());
        purchase.setTotalAmount(dto.getTotalAmount());

        return purchase;
    }

    // Entity -> DTO
public static PurchaseDTO toDTO(Purchase purchase) {

    PurchaseDTO dto = new PurchaseDTO();

    dto.setId(purchase.getId());
    dto.setPurchaseCode(purchase.getPurchaseCode());
    dto.setPurchaseDate(purchase.getPurchaseDate());
    dto.setTotalAmount(purchase.getTotalAmount());

    if (purchase.getSupplier() != null) {
        dto.setSupplierId(purchase.getSupplier().getId());
    }

    if (purchase.getPurchaseItems() != null) {

        List<PurchaseItemDTO> itemDTOs = purchase.getPurchaseItems()
                .stream()
                .map(PurchaseMapper::toPurchaseItemDTO)
                .collect(Collectors.toList());

        dto.setPurchaseItems(itemDTOs);
    }

    return dto;
}
private static PurchaseItemDTO toPurchaseItemDTO(PurchaseItem item) {

    PurchaseItemDTO dto = new PurchaseItemDTO();

    dto.setId(item.getId());

    if (item.getProduct() != null) {
        dto.setProductId(item.getProduct().getId());
    }

    dto.setQuantity(item.getQuantity());
    dto.setPurchasePrice(item.getPurchasePrice());
    dto.setTotalPrice(item.getTotalPrice());

    return dto;
}
}