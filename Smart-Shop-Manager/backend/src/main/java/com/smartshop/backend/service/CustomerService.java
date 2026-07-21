package com.smartshop.backend.service;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import com.smartshop.backend.dto.CustomerDTO;
import com.smartshop.backend.entity.Customer;
import com.smartshop.backend.mapper.CustomerMapper;
import com.smartshop.backend.repository.CustomerRepository;
import com.smartshop.backend.dto.CustomerStatisticsDTO;
@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    // Save Customer
    public CustomerDTO saveCustomer(CustomerDTO customerDTO) {

        Customer customer = CustomerMapper.toEntity(customerDTO);

        Customer savedCustomer = customerRepository.save(customer);

        return CustomerMapper.toDTO(savedCustomer);
    }

    // Get All Customers
    public List<CustomerDTO> getAllCustomers() {

        return customerRepository.findAll()
                .stream()
                .map(CustomerMapper::toDTO)
                .collect(Collectors.toList());
    }
    // Get Customer By ID
public CustomerDTO getCustomerById(Long id) {

    Customer customer = customerRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Customer not found"));

    return CustomerMapper.toDTO(customer);
}
// Update Customer
public CustomerDTO updateCustomer(Long id, CustomerDTO customerDTO) {

    Customer customer = customerRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Customer not found"));

    customer.setCustomerCode(customerDTO.getCustomerCode());
    customer.setCustomerName(customerDTO.getCustomerName());
    customer.setPhone(customerDTO.getPhone());
    customer.setEmail(customerDTO.getEmail());
    customer.setAddress(customerDTO.getAddress());
    customer.setCreditLimit(customerDTO.getCreditLimit());
    customer.setActive(customerDTO.getActive());

    Customer updatedCustomer = customerRepository.save(customer);

    return CustomerMapper.toDTO(updatedCustomer);
}
// Delete Customer
public String deleteCustomer(Long id) {

    Customer customer = customerRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Customer not found"));

    customerRepository.delete(customer);

    return "Customer deleted successfully!";
}
// Search Customers
public List<CustomerDTO> searchCustomers(String keyword) {

    return customerRepository
            .findByCustomerNameContainingIgnoreCase(keyword)
            .stream()
            .map(CustomerMapper::toDTO)
            .toList();
}
// Get Customers with Pagination
public Page<CustomerDTO> getCustomersByPage(int page, int size) {

    Page<Customer> customerPage =
            customerRepository.findAll(PageRequest.of(page, size));

    return customerPage.map(CustomerMapper::toDTO);
}
// Get Customers with Sorting
public List<CustomerDTO> getCustomersSorted(String field) {

    return customerRepository.findAll(Sort.by(Sort.Direction.ASC, field))
            .stream()
            .map(CustomerMapper::toDTO)
            .toList();
}
// Search + Pagination + Sorting
public Page<CustomerDTO> filterCustomers(
        String keyword,
        int page,
        int size,
        String sortField) {

    Pageable pageable = PageRequest.of(
            page,
            size,
            Sort.by(sortField));

    Page<Customer> customerPage =
            customerRepository.findByCustomerNameContainingIgnoreCase(
                    keyword,
                    pageable);

    return customerPage.map(CustomerMapper::toDTO);
}
// Customer Statistics
public CustomerStatisticsDTO getCustomerStatistics() {

    long total = customerRepository.count();
    long active = customerRepository.countActiveCustomers();
    long inactive = customerRepository.countInactiveCustomers();

    return new CustomerStatisticsDTO(
            total,
            active,
            inactive);
}
}