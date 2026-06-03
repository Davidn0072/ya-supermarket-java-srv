package com.supermarket.search.repository;

import com.supermarket.search.document.ProductDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface ProductSearchRepository extends ElasticsearchRepository<ProductDocument, String> {
    List<ProductDocument> findByNameContainingOrDescriptionContaining(String name, String description);
}
