package com.supermarket.order.service;

import com.supermarket.order.client.ProductClient;
import com.supermarket.order.dto.OrderItemRequest;
import com.supermarket.order.dto.ProductResponse;
import com.supermarket.order.entity.Order;
import com.supermarket.order.entity.OrderItem;
import com.supermarket.order.entity.OrderStatus;
import com.supermarket.order.event.OrderEvent;
import com.supermarket.order.repository.OrderRepository;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductClient productClient;
    private final KafkaTemplate<String, OrderEvent> kafkaTemplate;

    public OrderService(OrderRepository orderRepository,
                        ProductClient productClient,
                        KafkaTemplate<String, OrderEvent> kafkaTemplate) {
        this.orderRepository = orderRepository;
        this.productClient = productClient;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Transactional
    public Order createOrder(List<OrderItemRequest> itemRequests, String customerUsername, String jwtToken) {
        Order order = new Order();
        order.setCustomerUsername(customerUsername);
        order.setStatus(OrderStatus.PENDING);
        order.setCreatedAt(LocalDateTime.now());

        BigDecimal total = BigDecimal.ZERO;

        for (OrderItemRequest req : itemRequests) {
            ProductResponse product = productClient.getProduct(req.getProductId());
            if (product == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Product not found: " + req.getProductId());
            }

            productClient.decrementStock(req.getProductId(), req.getQuantity(), jwtToken);

            OrderItem item = new OrderItem();
            item.setOrder(order);
            item.setProductId(product.getId());
            item.setProductName(product.getName());
            item.setQuantity(req.getQuantity());
            item.setPriceAtPurchase(product.getPrice());
            order.getItems().add(item);

            total = total.add(product.getPrice().multiply(BigDecimal.valueOf(req.getQuantity())));
        }

        order.setTotalPrice(total);
        Order saved = orderRepository.save(order);
        kafkaTemplate.send("order-events", new OrderEvent("CREATE", saved));
        return saved;
    }

    public Order findById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found"));
    }

    public List<Order> findAll() {
        return orderRepository.findAll();
    }

    @Transactional
    public Order updateStatus(Long id, String status) {
        Order order = findById(id);
        try {
            order.setStatus(OrderStatus.valueOf(status));
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid status: " + status);
        }
        Order saved = orderRepository.save(order);
        kafkaTemplate.send("order-events", new OrderEvent("STATUS_CHANGE", saved));
        return saved;
    }
}
