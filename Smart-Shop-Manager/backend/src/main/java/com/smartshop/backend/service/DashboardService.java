package com.smartshop.backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import com.smartshop.backend.entity.Sale;
import com.smartshop.backend.dto.DashboardDTO;
import com.smartshop.backend.repository.CategoryRepository;
import com.smartshop.backend.repository.CustomerRepository;
import com.smartshop.backend.repository.ProductRepository;
import com.smartshop.backend.repository.PurchaseRepository;
import com.smartshop.backend.repository.SaleRepository;
import com.smartshop.backend.repository.SupplierRepository;
import com.smartshop.backend.entity.Purchase;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final CustomerRepository customerRepository;
    private final SupplierRepository supplierRepository;
    private final PurchaseRepository purchaseRepository;
    private final SaleRepository saleRepository;
   public DashboardDTO getDashboardData() {

    DashboardDTO dashboard = new DashboardDTO();

    dashboard.setTotalProducts(productRepository.count());
    dashboard.setTotalCategories(categoryRepository.count());
    dashboard.setTotalCustomers(customerRepository.count());
    dashboard.setTotalSuppliers(supplierRepository.count());

    double todaySales = saleRepository.findBySaleDate(LocalDate.now())
            .stream()
            .mapToDouble(Sale::getTotalAmount)
            .sum();

    dashboard.setTodaySales(todaySales);

    double todayPurchases = purchaseRepository.findByPurchaseDate(LocalDate.now())
            .stream()
            .mapToDouble(Purchase::getTotalAmount)
            .sum();

    dashboard.setTodayPurchases(todayPurchases);

    dashboard.setLowStockProducts(
            productRepository.countByQuantityLessThanEqual(10)
    );

    return dashboard;
}
}