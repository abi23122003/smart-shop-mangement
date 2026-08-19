package com.smartshop.backend.mapper;

import java.util.List;
import java.util.stream.Collectors;

import com.smartshop.backend.dto.SaleDTO;
import com.smartshop.backend.dto.SaleItemDTO;
import com.smartshop.backend.entity.Customer;
import com.smartshop.backend.entity.Sale;
import com.smartshop.backend.entity.SaleItem;

public class SaleMapper {

    public static Sale toEntity(SaleDTO dto) {

        if (dto == null) {
            return null;
        }

        Sale sale = new Sale();

        sale.setId(dto.getId());
        sale.setSaleCode(dto.getSaleCode());
        sale.setSaleDate(dto.getSaleDate());
        sale.setPaymentMethod(dto.getPaymentMethod());
        sale.setTotalAmount(dto.getTotalAmount());

        if (dto.getCustomerId() != null) {
            Customer customer = new Customer();
            customer.setId(dto.getCustomerId());
            sale.setCustomer(customer);
        }

        return sale;
    }

    public static SaleDTO toDTO(Sale sale) {

        if (sale == null) {
            return null;
        }

        SaleDTO dto = new SaleDTO();

        dto.setId(sale.getId());
        dto.setSaleCode(sale.getSaleCode());
        dto.setSaleDate(sale.getSaleDate());
        dto.setPaymentMethod(sale.getPaymentMethod());
        dto.setTotalAmount(sale.getTotalAmount());

        if (sale.getCustomer() != null) {
            dto.setCustomerId(sale.getCustomer().getId());
        }

        if (sale.getSaleItems() != null) {
            List<SaleItemDTO> items = sale.getSaleItems()
                    .stream()
                    .map(SaleMapper::toSaleItemDTO)
                    .collect(Collectors.toList());

            dto.setSaleItems(items);
        }

        return dto;
    }

    private static SaleItemDTO toSaleItemDTO(SaleItem saleItem) {

        SaleItemDTO dto = new SaleItemDTO();

        dto.setId(saleItem.getId());

        if (saleItem.getProduct() != null) {
            dto.setProductId(saleItem.getProduct().getId());
        }

        dto.setQuantity(saleItem.getQuantity());
        dto.setSellingPrice(saleItem.getSellingPrice());
        dto.setTotalPrice(saleItem.getTotalPrice());

        return dto;
    }
}