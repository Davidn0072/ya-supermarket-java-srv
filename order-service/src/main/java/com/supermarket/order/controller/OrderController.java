package com.supermarket.order.controller;

import com.supermarket.order.dto.OrderItemRequest;
import com.supermarket.order.dto.StatusUpdateRequest;
import com.supermarket.order.entity.Order;
import com.supermarket.order.service.OrderService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("isAuthenticated()")
    public Order createOrder(@RequestBody List<OrderItemRequest> items,
                             Authentication authentication,
                             HttpServletRequest request) {
        String jwtToken = extractJwtFromCookie(request);
        return orderService.createOrder(items, authentication.getName(), jwtToken);
    }

    @GetMapping("/{id}")
    @PreAuthorize("@orderAuthz.canRead(#id)")
    public Order findById(@PathVariable("id") Long id) {
        return orderService.findById(id);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<Order> findAll() {
        return orderService.findAll();
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public Order updateStatus(@PathVariable("id") Long id,
                              @RequestBody StatusUpdateRequest request) {
        return orderService.updateStatus(id, request.getStatus());
    }

    private String extractJwtFromCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) return null;
        return Arrays.stream(cookies)
                .filter(c -> "ACCESS_TOKEN".equals(c.getName()))
                .map(Cookie::getValue)
                .findFirst().orElse(null);
    }
}
