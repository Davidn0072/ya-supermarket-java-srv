package com.supermarket.customer.controller;

import com.supermarket.customer.dto.CustomerUpdateRequest;
import com.supermarket.customer.entity.Customer;
import com.supermarket.customer.service.CustomerService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("/{id}")
    @PreAuthorize("@customerAuthz.canAccess(#id)")
    public Customer findById(@PathVariable("id") Long id) {
        return customerService.findById(id);
    }

    @PutMapping("/{id}")
    @PreAuthorize("@customerAuthz.canAccess(#id)")
    public Customer update(@PathVariable("id") Long id, @RequestBody CustomerUpdateRequest request) {
        return customerService.update(id, request);
    }
}
