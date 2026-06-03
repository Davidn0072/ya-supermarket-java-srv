package com.supermarket.product.config;

import com.supermarket.product.entity.Category;
import com.supermarket.product.entity.Product;
import com.supermarket.product.repository.CategoryRepository;
import com.supermarket.product.repository.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class DataInitializer implements CommandLineRunner {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    public DataInitializer(CategoryRepository categoryRepository, ProductRepository productRepository) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
    }

    @Override
    public void run(String... args) {
        if (categoryRepository.count() > 0) return;

        Category dairy = categoryRepository.save(new Category(null, "Dairy"));
        Category bakery = categoryRepository.save(new Category(null, "Bakery"));
        Category produce = categoryRepository.save(new Category(null, "Produce"));

        Product p1 = new Product();
        p1.setSku("MILK-3PCT-1L"); p1.setName("Milk 3% 1L"); p1.setPrice(new BigDecimal("5.90"));
        p1.setStockQuantity(100); p1.setCategory(dairy);
        productRepository.save(p1);

        Product p2 = new Product();
        p2.setSku("BREAD-WHITE-500G"); p2.setName("White Bread 500g"); p2.setPrice(new BigDecimal("7.50"));
        p2.setStockQuantity(50); p2.setCategory(bakery);
        productRepository.save(p2);

        Product p3 = new Product();
        p3.setSku("EGG-L-12PK"); p3.setName("Eggs Large 12-pack"); p3.setPrice(new BigDecimal("18.90"));
        p3.setStockQuantity(80); p3.setCategory(dairy);
        productRepository.save(p3);

        Product p4 = new Product();
        p4.setSku("TOMATO-1KG"); p4.setName("Tomatoes 1kg"); p4.setPrice(new BigDecimal("9.90"));
        p4.setStockQuantity(60); p4.setCategory(produce);
        productRepository.save(p4);
    }
}
