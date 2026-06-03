package com.supermarket.product.dto;

import java.math.BigDecimal;

public class ProductRequest {
    private String sku;
    private String name;
    private String description;
    private BigDecimal price;
    private Integer stockQuantity;
    private Long categoryId;
    private String imageUrl;

    public String getSku() { return sku; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public BigDecimal getPrice() { return price; }
    public Integer getStockQuantity() { return stockQuantity; }
    public Long getCategoryId() { return categoryId; }
    public String getImageUrl() { return imageUrl; }

    public void setSku(String sku) { this.sku = sku; }
    public void setName(String name) { this.name = name; }
    public void setDescription(String description) { this.description = description; }
    public void setPrice(BigDecimal price) { this.price = price; }
    public void setStockQuantity(Integer stockQuantity) { this.stockQuantity = stockQuantity; }
    public void setCategoryId(Long categoryId) { this.categoryId = categoryId; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
}
