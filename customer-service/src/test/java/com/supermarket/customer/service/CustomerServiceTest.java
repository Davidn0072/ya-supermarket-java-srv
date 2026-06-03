package com.supermarket.customer.service;

import com.supermarket.customer.dto.CustomerUpdateRequest;
import com.supermarket.customer.entity.Customer;
import com.supermarket.customer.repository.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock CustomerRepository customerRepository;
    @InjectMocks CustomerService customerService;

    @Test
    void findById_existing_returnsCustomer() {
        Customer customer = new Customer(1L, "user", "user@test.com", "123 St", "050-1");
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));

        Customer result = customerService.findById(1L);
        assertEquals("user", result.getUsername());
    }

    @Test
    void findById_notFound_throws404() {
        when(customerRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(org.springframework.web.server.ResponseStatusException.class,
                () -> customerService.findById(99L));
    }

    @Test
    void update_existingCustomer_updatesFields() {
        Customer customer = new Customer(1L, "user", "old@test.com", "Old St", "050-0");
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(customerRepository.save(any())).thenReturn(customer);

        CustomerUpdateRequest req = new CustomerUpdateRequest();
        req.setEmail("new@test.com");
        req.setAddress("New St");
        req.setPhone("050-9");

        Customer result = customerService.update(1L, req);
        assertEquals("new@test.com", result.getEmail());
        assertEquals("New St", result.getAddress());
    }
}
