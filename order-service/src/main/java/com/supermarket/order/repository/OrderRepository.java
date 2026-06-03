package com.supermarket.order.repository;

import com.supermarket.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByCustomerUsername(String customerUsername);
}
