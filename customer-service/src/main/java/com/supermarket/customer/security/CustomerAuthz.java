package com.supermarket.customer.security;

import com.supermarket.customer.repository.CustomerRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component("customerAuthz")
public class CustomerAuthz {

    private final CustomerRepository customerRepository;

    public CustomerAuthz(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public boolean canAccess(Long customerId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) return false;

        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        if (isAdmin) return true;

        return customerRepository.findById(customerId)
                .map(c -> c.getUsername().equals(auth.getName()))
                .orElse(false);
    }
}
