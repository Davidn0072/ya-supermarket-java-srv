package com.supermarket.product.controller;

import com.supermarket.product.dto.ProductRequest;
import com.supermarket.product.dto.StockDecrementRequest;
import com.supermarket.product.entity.Product;
import com.supermarket.product.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public List<Product> findAll(@RequestParam(name = "categoryId", required = false) Long categoryId) {
        return productService.findAll(categoryId);
    }

    @GetMapping("/{id}")
    public Product findById(@PathVariable("id") Long id) {
        return productService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    public Product create(@RequestBody ProductRequest request) {
        return productService.create(request);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Product update(@PathVariable("id") Long id, @RequestBody ProductRequest request) {
        return productService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN')")
    public void delete(@PathVariable("id") Long id) {
        productService.delete(id);
    }

    @PatchMapping("/{id}/stock")
    @PreAuthorize("isAuthenticated()")
    public void decrementStock(@PathVariable("id") Long id, @RequestBody StockDecrementRequest request) {
        productService.decrementStock(id, request);
    }
}
