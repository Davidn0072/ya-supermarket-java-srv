package com.supermarket.product.service;

import com.supermarket.product.dto.ProductRequest;
import com.supermarket.product.dto.StockDecrementRequest;
import com.supermarket.product.entity.Category;
import com.supermarket.product.entity.Product;
import com.supermarket.product.event.ProductEvent;
import com.supermarket.product.repository.CategoryRepository;
import com.supermarket.product.repository.ProductRepository;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final KafkaTemplate<String, ProductEvent> kafkaTemplate;

    public ProductService(ProductRepository productRepository,
                          CategoryRepository categoryRepository,
                          KafkaTemplate<String, ProductEvent> kafkaTemplate) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    public List<Product> findAll(Long categoryId) {
        if (categoryId != null) return productRepository.findByCategoryId(categoryId);
        return productRepository.findAll();
    }

    public Product findById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));
    }

    @Transactional
    public Product create(ProductRequest request) {
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Category not found"));

        Product product = new Product();
        product.setSku(request.getSku());
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setStockQuantity(request.getStockQuantity());
        product.setCategory(category);
        product.setImageUrl(request.getImageUrl());

        Product saved = productRepository.save(product);
        kafkaTemplate.send("product-events", new ProductEvent("CREATE", saved));
        return saved;
    }

    @Transactional
    public Product update(Long id, ProductRequest request) {
        Product product = findById(id);
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Category not found"));

        product.setSku(request.getSku());
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setStockQuantity(request.getStockQuantity());
        product.setCategory(category);
        product.setImageUrl(request.getImageUrl());

        Product saved = productRepository.save(product);
        kafkaTemplate.send("product-events", new ProductEvent("UPDATE", saved));
        return saved;
    }

    @Transactional
    public void delete(Long id) {
        Product product = findById(id);
        productRepository.delete(product);
        kafkaTemplate.send("product-events", new ProductEvent("DELETE", product));
    }

    @Transactional
    public void decrementStock(Long id, StockDecrementRequest request) {
        Product product = findById(id);
        if (product.getStockQuantity() < request.getQuantity()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Insufficient stock for product: " + id);
        }
        product.setStockQuantity(product.getStockQuantity() - request.getQuantity());
        productRepository.save(product);
    }
}
