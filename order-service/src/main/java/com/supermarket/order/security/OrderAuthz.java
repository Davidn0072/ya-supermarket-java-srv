package com.supermarket.order.security;

import com.supermarket.order.repository.OrderRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component("orderAuthz")
public class OrderAuthz {

    private final OrderRepository orderRepository;

    public OrderAuthz(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public boolean canRead(Long orderId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) return false;

        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        if (isAdmin) return true;

        return orderRepository.findById(orderId)
                .map(order -> order.getCustomerUsername().equals(auth.getName()))
                .orElse(false);
    }
}
