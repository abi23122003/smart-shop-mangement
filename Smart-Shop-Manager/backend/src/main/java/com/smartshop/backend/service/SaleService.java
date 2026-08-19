package com.smartshop.backend.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smartshop.backend.dto.SaleDTO;
import com.smartshop.backend.dto.SaleItemDTO;
import com.smartshop.backend.entity.Credit;
import com.smartshop.backend.entity.CreditTransaction;
import com.smartshop.backend.entity.Customer;
import com.smartshop.backend.entity.Product;
import com.smartshop.backend.entity.Sale;
import com.smartshop.backend.entity.SaleItem;
import com.smartshop.backend.mapper.SaleMapper;
import com.smartshop.backend.repository.CreditRepository;
import com.smartshop.backend.repository.CreditTransactionRepository;
import com.smartshop.backend.repository.CustomerRepository;
import com.smartshop.backend.repository.ProductRepository;
import com.smartshop.backend.repository.SaleRepository;


@Service
public class SaleService {

    private final SaleRepository saleRepository;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;
    private final CreditRepository creditRepository;
    private final CreditTransactionRepository creditTransactionRepository;

    public SaleService(
            SaleRepository saleRepository,
            CustomerRepository customerRepository,
            ProductRepository productRepository,
            CreditRepository creditRepository,
            CreditTransactionRepository creditTransactionRepository) {

        this.saleRepository = saleRepository;
        this.customerRepository = customerRepository;
        this.productRepository = productRepository;
        this.creditRepository = creditRepository;
        this.creditTransactionRepository = creditTransactionRepository;
    }
    @Transactional
public SaleDTO saveSale(SaleDTO saleDTO) {

   Customer customer = customerRepository.findById(saleDTO.getCustomerId())
        .orElseThrow(() -> new RuntimeException("Customer not found"));

   if ("Credit".equalsIgnoreCase(saleDTO.getPaymentMethod()) && Boolean.FALSE.equals(customer.getCreditEnabled())) {
       throw new IllegalArgumentException("This customer is not enabled for credit purchases");
   }

    

Sale sale = new Sale();

sale.setSaleCode(saleDTO.getSaleCode());
sale.setSaleDate(saleDTO.getSaleDate());
sale.setCustomer(customer);
sale.setPaymentMethod(saleDTO.getPaymentMethod());

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

if ("Credit".equalsIgnoreCase(saleDTO.getPaymentMethod())) {
    Credit credit = creditRepository.findByCustomer(customer).orElseGet(() -> {
        Credit created = new Credit();
        created.setCustomer(customer);
        created.setTotalCredit(0.0);
        created.setTotalPaid(0.0);
        created.setBalance(0.0);
        created.setStatus("PENDING");
        return creditRepository.save(created);
    });
    double balance = valueOrZero(credit.getBalance());
    double totalCredit = valueOrZero(credit.getTotalCredit());
    credit.setBalance(balance + totalAmount);
    credit.setTotalCredit(totalCredit + totalAmount);
    credit.setStatus("PENDING");
    creditRepository.save(credit);

    CreditTransaction transaction = new CreditTransaction();
    transaction.setCredit(credit);
    transaction.setTransactionDate(saleDTO.getSaleDate());
    transaction.setType("PURCHASE");
    transaction.setAmount(totalAmount);
    transaction.setRemarks("Credit sale: " + saleDTO.getSaleCode());
    creditTransactionRepository.save(transaction);
}

return SaleMapper.toDTO(savedSale);
}

private double valueOrZero(Double value) {
    return value == null ? 0.0 : value;
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
