package com.supermarket.search.dto;

import java.math.BigDecimal;

public class OrderEvent {
    private String eventType;
    private Long orderId;
    private String customerUsername;
    private String status;
    private BigDecimal totalPrice;

    public String getEventType() { return eventType; }
    public Long getOrderId() { return orderId; }
    public String getCustomerUsername() { return customerUsername; }
    public String getStatus() { return status; }
    public BigDecimal getTotalPrice() { return totalPrice; }

    public void setEventType(String eventType) { this.eventType = eventType; }
    public void setOrderId(Long orderId) { this.orderId = orderId; }
    public void setCustomerUsername(String customerUsername) { this.customerUsername = customerUsername; }
    public void setStatus(String status) { this.status = status; }
    public void setTotalPrice(BigDecimal totalPrice) { this.totalPrice = totalPrice; }
}
