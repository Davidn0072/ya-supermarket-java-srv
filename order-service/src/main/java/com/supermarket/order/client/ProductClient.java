package com.supermarket.order.client;

import com.supermarket.order.dto.ProductResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
public class ProductClient {

    private final RestTemplate restTemplate;

    @Value("${product.service.url:http://localhost:8080}")
    private String productServiceUrl;

    public ProductClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public ProductResponse getProduct(Long productId) {
        return restTemplate.getForObject(
                productServiceUrl + "/api/products/" + productId,
                ProductResponse.class
        );
    }

    public void decrementStock(Long productId, int quantity, String jwtToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Cookie", "ACCESS_TOKEN=" + jwtToken);

        HttpEntity<Map<String, Integer>> entity = new HttpEntity<>(
                Map.of("quantity", quantity), headers
        );

        restTemplate.exchange(
                productServiceUrl + "/api/products/" + productId + "/stock",
                HttpMethod.PATCH, entity, Void.class
        );
    }
}
