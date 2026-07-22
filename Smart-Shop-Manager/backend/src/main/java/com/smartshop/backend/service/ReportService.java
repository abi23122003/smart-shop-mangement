package com.smartshop.backend.service;

import java.util.List;

import org.springframework.stereotype.Service;
import com.smartshop.backend.dto.SalesReportDTO;
import com.smartshop.backend.entity.Sale;
import com.smartshop.backend.repository.SaleRepository;
import lombok.RequiredArgsConstructor;
import com.smartshop.backend.dto.PurchaseReportDTO;
import com.smartshop.backend.entity.Purchase;
import com.smartshop.backend.entity.Product;
import com.smartshop.backend.repository.PurchaseRepository;
import com.smartshop.backend.dto.ProductReportDTO;
import com.smartshop.backend.dto.StockReportDTO;
import com.smartshop.backend.dto.CustomerReportDTO;
import com.smartshop.backend.entity.Customer;
import com.smartshop.backend.repository.CustomerRepository;
import com.smartshop.backend.repository.ProductRepository;
import com.smartshop.backend.dto.SupplierReportDTO;
import com.smartshop.backend.entity.Supplier;
import com.smartshop.backend.repository.SupplierRepository;
@Service
@RequiredArgsConstructor
public class ReportService {

    private final SaleRepository saleRepository;
    private final PurchaseRepository purchaseRepository;
    private final ProductRepository productRepository;
    private final CustomerRepository customerRepository;
    private final SupplierRepository supplierRepository;
    
    public List<SalesReportDTO> getSalesReport() {

    return saleRepository.findAll()
            .stream()
            .map(this::convertToDTO)
            .toList();
}
public List<PurchaseReportDTO> getPurchaseReport() {

    return purchaseRepository.findAll()
            .stream()
            .map(this::convertPurchaseToDTO)
            .toList();
}
private PurchaseReportDTO convertPurchaseToDTO(Purchase purchase) {

    PurchaseReportDTO dto = new PurchaseReportDTO();

    dto.setPurchaseCode(purchase.getPurchaseCode());

    if (purchase.getSupplier() != null) {
        dto.setSupplierName(purchase.getSupplier().getSupplierName());
    } else {
        dto.setSupplierName("No Supplier");
    }

    dto.setPurchaseDate(purchase.getPurchaseDate().toString());
    dto.setTotalAmount(purchase.getTotalAmount());

    return dto;
}
public List<ProductReportDTO> getProductReport() {

    return productRepository.findAll()
            .stream()
            .map(this::convertProductToDTO)
            .toList();
}

public List<StockReportDTO> getStockReport() {

    return productRepository.findAll()
            .stream()
            .map(this::convertStockToDTO)
            .toList();
}
public List<CustomerReportDTO> getCustomerReport() {

    return customerRepository.findAll()
            .stream()
            .map(this::convertCustomerToDTO)
            .toList();
}
public List<SupplierReportDTO> getSupplierReport() {

    return supplierRepository.findAll()
            .stream()
            .map(this::convertSupplierToDTO)
            .toList();
}
private StockReportDTO convertStockToDTO(Product product) {

    StockReportDTO dto = new StockReportDTO();

    dto.setProductCode(product.getProductCode());
    dto.setProductName(product.getProductName());
    dto.setQuantity(product.getQuantity());
    dto.setMinimumStock(product.getMinimumStock());

    if (product.getQuantity() <= product.getMinimumStock()) {
        dto.setStockStatus("Low Stock");
    } else {
        dto.setStockStatus("In Stock");
    }

    return dto;
}
private CustomerReportDTO convertCustomerToDTO(Customer customer) {

    CustomerReportDTO dto = new CustomerReportDTO();

    dto.setCustomerName(customer.getCustomerName());
    dto.setPhone(customer.getPhone());
    dto.setEmail(customer.getEmail());

    return dto;
}
private SupplierReportDTO convertSupplierToDTO(Supplier supplier) {

    SupplierReportDTO dto = new SupplierReportDTO();

    dto.setSupplierName(supplier.getSupplierName());
    dto.setPhone(supplier.getPhone());
    dto.setEmail(supplier.getEmail());

    return dto;
}
private ProductReportDTO convertProductToDTO(Product product) {
    

    ProductReportDTO dto = new ProductReportDTO();

    dto.setProductCode(product.getProductCode());
    dto.setProductName(product.getProductName());

    if (product.getCategory() != null) {
       dto.setCategoryName(product.getCategory().getName());
    } else {
        dto.setCategoryName("No Category");
    }

    dto.setBrand(product.getBrand());
    dto.setQuantity(product.getQuantity());
    dto.setPurchasePrice(product.getPurchasePrice());
    dto.setSellingPrice(product.getSellingPrice());

    return dto;
}
private SalesReportDTO convertToDTO(Sale sale) {

    SalesReportDTO dto = new SalesReportDTO();

    dto.setSaleCode(sale.getSaleCode());
    dto.setCustomerName(sale.getCustomer().getCustomerName());
    dto.setSaleDate(sale.getSaleDate().toString());
    dto.setTotalAmount(sale.getTotalAmount());

    return dto;
}

}