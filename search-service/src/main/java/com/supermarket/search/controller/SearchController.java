package com.supermarket.search.controller;

import com.supermarket.search.document.ProductDocument;
import com.supermarket.search.repository.ProductSearchRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/search")
public class SearchController {

    private final ProductSearchRepository searchRepository;

    public SearchController(ProductSearchRepository searchRepository) {
        this.searchRepository = searchRepository;
    }

    @GetMapping("/products")
    public List<ProductDocument> search(@RequestParam("q") String query) {
        return searchRepository.findByNameContainingOrDescriptionContaining(query, query);
    }
}
