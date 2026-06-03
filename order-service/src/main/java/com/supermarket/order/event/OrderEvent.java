package com.supermarket.order.event;

import com.supermarket.order.entity.Order;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class OrderEvent {

    private String eventType; // CREATE, STATUS_CHANGE
    private Long orderId;
    private String customerUsername;
    private String status;
    private BigDecimal totalPrice;
    private LocalDateTime createdAt;

    public OrderEvent() {}

    public OrderEvent(String eventType, Order order) {
        this.eventType = eventType;
        this.orderId = order.getId();
        this.customerUsername = order.getCustomerUsername();
        this.status = order.getStatus().name();
        this.totalPrice = order.getTotalPrice();
        this.createdAt = order.getCreatedAt();
    }

    public String getEventType() { return eventType; }
    public Long getOrderId() { return orderId; }
    public String getCustomerUsername() { return customerUsername; }
    public String getStatus() { return status; }
    public BigDecimal getTotalPrice() { return totalPrice; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    public void setEventType(String eventType) { this.eventType = eventType; }
    public void setOrderId(Long orderId) { this.orderId = orderId; }
    public void setCustomerUsername(String customerUsername) { this.customerUsername = customerUsername; }
    public void setStatus(String status) { this.status = status; }
    public void setTotalPrice(BigDecimal totalPrice) { this.totalPrice = totalPrice; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
