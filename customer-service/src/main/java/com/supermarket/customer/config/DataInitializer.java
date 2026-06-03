package com.supermarket.customer.config;

import com.supermarket.customer.entity.Customer;
import com.supermarket.customer.repository.CustomerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final CustomerRepository customerRepository;

    public DataInitializer(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public void run(String... args) {
        if (customerRepository.count() > 0) return;
        customerRepository.save(new Customer(null, "user", "user@supermarket.com", "123 Main St", "050-0000001"));
        customerRepository.save(new Customer(null, "admin", "admin@supermarket.com", "1 Admin Ave", "050-0000002"));
    }
}
