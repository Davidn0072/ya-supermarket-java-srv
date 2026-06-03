package com.supermarket.search.dto;

import java.math.BigDecimal;

public class ProductEvent {
    private String eventType;
    private Long productId;
    private String sku;
    private String name;
    private String description;
    private BigDecimal price;
    private Integer stockQuantity;
    private String categoryName;
    private String imageUrl;

    public String getEventType() { return eventType; }
    public Long getProductId() { return productId; }
    public String getSku() { return sku; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public BigDecimal getPrice() { return price; }
    public Integer getStockQuantity() { return stockQuantity; }
    public String getCategoryName() { return categoryName; }
    public String getImageUrl() { return imageUrl; }

    public void setEventType(String eventType) { this.eventType = eventType; }
    public void setProductId(Long productId) { this.productId = productId; }
    public void setSku(String sku) { this.sku = sku; }
    public void setName(String name) { this.name = name; }
    public void setDescription(String description) { this.description = description; }
    public void setPrice(BigDecimal price) { this.price = price; }
    public void setStockQuantity(Integer stockQuantity) { this.stockQuantity = stockQuantity; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
}
