package com.smartshop.backend.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;

import com.smartshop.backend.dto.SaleDTO;
import com.smartshop.backend.dto.SaleItemDTO;
import com.smartshop.backend.entity.Customer;
import com.smartshop.backend.entity.Product;
import com.smartshop.backend.entity.Sale;
import com.smartshop.backend.entity.SaleItem;
import com.smartshop.backend.mapper.SaleMapper;
import com.smartshop.backend.repository.CustomerRepository;
import com.smartshop.backend.repository.ProductRepository;
import com.smartshop.backend.repository.SaleRepository;


@Service
public class SaleService {

    private final SaleRepository saleRepository;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;

    public SaleService(
            SaleRepository saleRepository,
            CustomerRepository customerRepository,
            ProductRepository productRepository) {

        this.saleRepository = saleRepository;
        this.customerRepository = customerRepository;
        this.productRepository = productRepository;
    }
    @Transactional
public SaleDTO saveSale(SaleDTO saleDTO) {

   Customer customer = customerRepository.findById(saleDTO.getCustomerId())
        .orElseThrow(() -> new RuntimeException("Customer not found"));

    

Sale sale = new Sale();

sale.setSaleCode(saleDTO.getSaleCode());
sale.setSaleDate(saleDTO.getSaleDate());
sale.setCustomer(customer);

double totalAmount = 0.0;

List<SaleItem> saleItems = new ArrayList<>();
for (SaleItemDTO itemDTO : saleDTO.getSaleItems()) {

    Product product = productRepository.findById(itemDTO.getProductId())
            .orElseThrow(() -> new RuntimeException("Product not found"));

    if (product.getQuantity() < itemDTO.getQuantity()) {
        throw new RuntimeException(
                "Insufficient stock for product: " + product.getProductName());
    }

    product.setQuantity(product.getQuantity() - itemDTO.getQuantity());
    productRepository.save(product);

    SaleItem saleItem = new SaleItem();

    saleItem.setSale(sale);
    saleItem.setProduct(product);
    saleItem.setQuantity(itemDTO.getQuantity());
    saleItem.setSellingPrice(itemDTO.getSellingPrice());

    double itemTotal =
            itemDTO.getQuantity() * itemDTO.getSellingPrice();

    saleItem.setTotalPrice(itemTotal);

    totalAmount += itemTotal;

    saleItems.add(saleItem);
}
sale.setSaleItems(saleItems);
sale.setTotalAmount(totalAmount);

Sale savedSale = saleRepository.save(sale);

return SaleMapper.toDTO(savedSale);
}
public List<SaleDTO> getAllSales() {

    return saleRepository.findAll()
            .stream()
            .map(SaleMapper::toDTO)
            .toList();
}
public SaleDTO getSaleById(Long id) {

    Sale sale = saleRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Sale not found"));

    return SaleMapper.toDTO(sale);
}
@Transactional
public void deleteSale(Long id) {

    Sale sale = saleRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Sale not found"));

    for (SaleItem saleItem : sale.getSaleItems()) {
        Product product = saleItem.getProduct();
        product.setQuantity(product.getQuantity() + saleItem.getQuantity());
        productRepository.save(product);
    }

    saleRepository.delete(sale);
}
public SaleDTO updateSale(Long id, SaleDTO saleDTO) {

    throw new UnsupportedOperationException(
            "Updating completed sales is not supported. Please cancel the sale and create a new one.");
}
}
