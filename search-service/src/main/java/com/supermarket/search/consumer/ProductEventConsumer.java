package com.supermarket.search.consumer;

import com.supermarket.search.document.ProductDocument;
import com.supermarket.search.dto.ProductEvent;
import com.supermarket.search.repository.ProductSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class ProductEventConsumer {

    private static final Logger log = LoggerFactory.getLogger(ProductEventConsumer.class);

    private final ProductSearchRepository searchRepository;

    public ProductEventConsumer(ProductSearchRepository searchRepository) {
        this.searchRepository = searchRepository;
    }

    @KafkaListener(topics = "product-events", groupId = "search-service")
    public void handle(ProductEvent event) {
        log.info("Received product event: {} for product {}", event.getEventType(), event.getProductId());

        if ("DELETE".equals(event.getEventType())) {
            searchRepository.deleteById(String.valueOf(event.getProductId()));
            return;
        }

        ProductDocument doc = new ProductDocument();
        doc.setId(String.valueOf(event.getProductId()));
        doc.setSku(event.getSku());
        doc.setName(event.getName());
        doc.setDescription(event.getDescription());
        doc.setPrice(event.getPrice());
        doc.setCategoryName(event.getCategoryName());
        doc.setImageUrl(event.getImageUrl());
        searchRepository.save(doc);
    }
}
