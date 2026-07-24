package com.smartshop.backend.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smartshop.backend.dto.PurchaseDTO;
import com.smartshop.backend.dto.PurchaseItemDTO;
import com.smartshop.backend.entity.Product;
import com.smartshop.backend.entity.Purchase;
import com.smartshop.backend.entity.PurchaseItem;
import com.smartshop.backend.entity.Supplier;
import com.smartshop.backend.mapper.PurchaseMapper;
import com.smartshop.backend.repository.ProductRepository;
import com.smartshop.backend.repository.PurchaseRepository;
import com.smartshop.backend.repository.SupplierRepository;
@Service
public class PurchaseService {

private final PurchaseRepository purchaseRepository;
private final SupplierRepository supplierRepository;
private final ProductRepository productRepository;

public PurchaseService(
        PurchaseRepository purchaseRepository,
        SupplierRepository supplierRepository,
        ProductRepository productRepository) {

    this.purchaseRepository = purchaseRepository;
    this.supplierRepository = supplierRepository;
    this.productRepository = productRepository;
}

   @Transactional
public PurchaseDTO savePurchase(PurchaseDTO purchaseDTO) {

    // Step 1: Find Supplier
    Supplier supplier = supplierRepository.findById(purchaseDTO.getSupplierId())
            .orElseThrow(() -> new RuntimeException("Supplier not found"));

    // Step 2: Create Purchase
    Purchase purchase = new Purchase();
    purchase.setPurchaseCode(purchaseDTO.getPurchaseCode());
    purchase.setPurchaseDate(purchaseDTO.getPurchaseDate());
    purchase.setSupplier(supplier);

    double totalAmount = 0.0;

    List<PurchaseItem> purchaseItems = new ArrayList<>();

    for (PurchaseItemDTO itemDTO : purchaseDTO.getPurchaseItems()) {

        Product product = productRepository.findById(itemDTO.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));
                // Update Product Stock
product.setQuantity(
        product.getQuantity() + itemDTO.getQuantity()
);

// Save Updated Product
productRepository.save(product);

        PurchaseItem purchaseItem = new PurchaseItem();

        purchaseItem.setPurchase(purchase);
        purchaseItem.setProduct(product);

        purchaseItem.setQuantity(itemDTO.getQuantity());
        purchaseItem.setPurchasePrice(itemDTO.getPurchasePrice());

        double itemTotal =
                itemDTO.getQuantity() * itemDTO.getPurchasePrice();

        purchaseItem.setTotalPrice(itemTotal);

        totalAmount += itemTotal;

        purchaseItems.add(purchaseItem);
    }

    purchase.setTotalAmount(totalAmount);
    purchase.setPurchaseItems(purchaseItems);

    Purchase savedPurchase = purchaseRepository.save(purchase);

    return PurchaseMapper.toDTO(savedPurchase);
}

    // Get All Purchases
    public List<PurchaseDTO> getAllPurchases() {

        return purchaseRepository.findAll()
                .stream()
                .map(PurchaseMapper::toDTO)
                .collect(Collectors.toList());
    }
}
