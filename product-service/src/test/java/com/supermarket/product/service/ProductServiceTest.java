package com.supermarket.product.service;

import com.supermarket.product.dto.ProductRequest;
import com.supermarket.product.entity.Category;
import com.supermarket.product.entity.Product;
import com.supermarket.product.event.ProductEvent;
import com.supermarket.product.repository.CategoryRepository;
import com.supermarket.product.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock ProductRepository productRepository;
    @Mock CategoryRepository categoryRepository;
    @Mock KafkaTemplate<String, ProductEvent> kafkaTemplate;
    @InjectMocks ProductService productService;

    @Test
    void findAll_noFilter_returnsAllProducts() {
        when(productRepository.findAll()).thenReturn(List.of(new Product(), new Product()));
        List<Product> result = productService.findAll(null);
        assertEquals(2, result.size());
    }

    @Test
    void findAll_withCategoryFilter_returnsFiltered() {
        when(productRepository.findByCategoryId(1L)).thenReturn(List.of(new Product()));
        List<Product> result = productService.findAll(1L);
        assertEquals(1, result.size());
    }

    @Test
    void findById_existing_returnsProduct() {
        Product product = new Product();
        product.setName("Milk");
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        assertEquals("Milk", productService.findById(1L).getName());
    }

    @Test
    void create_validRequest_savesAndPublishesEvent() {
        Category category = new Category(1L, "Dairy");
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));

        Product saved = new Product();
        saved.setId(1L);
        saved.setName("Milk");
        saved.setCategory(category);
        when(productRepository.save(any())).thenReturn(saved);

        ProductRequest request = new ProductRequest();
        request.setSku("MILK-1L"); request.setName("Milk");
        request.setPrice(new BigDecimal("5.90")); request.setStockQuantity(100);
        request.setCategoryId(1L);

        Product result = productService.create(request);

        assertEquals("Milk", result.getName());
        verify(kafkaTemplate).send(eq("product-events"), any(ProductEvent.class));
    }
}
