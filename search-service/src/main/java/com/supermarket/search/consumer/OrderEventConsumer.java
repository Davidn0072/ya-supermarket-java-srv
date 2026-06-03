package com.supermarket.search.consumer;

import com.supermarket.search.dto.OrderEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class OrderEventConsumer {

    private static final Logger log = LoggerFactory.getLogger(OrderEventConsumer.class);

    @KafkaListener(topics = "order-events", groupId = "search-service")
    public void handle(OrderEvent event) {
        log.info("Received order event: {} for order {}", event.getEventType(), event.getOrderId());
    }
}
