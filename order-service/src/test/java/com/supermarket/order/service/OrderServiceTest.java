package com.supermarket.order.service;

import com.supermarket.order.client.ProductClient;
import com.supermarket.order.dto.OrderItemRequest;
import com.supermarket.order.dto.ProductResponse;
import com.supermarket.order.entity.Order;
import com.supermarket.order.entity.OrderStatus;
import com.supermarket.order.event.OrderEvent;
import com.supermarket.order.repository.OrderRepository;
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
class OrderServiceTest {

    @Mock OrderRepository orderRepository;
    @Mock ProductClient productClient;
    @Mock KafkaTemplate<String, OrderEvent> kafkaTemplate;
    @InjectMocks OrderService orderService;

    @Test
    void createOrder_validItems_savesOrderAndPublishesEvent() {
        ProductResponse product = new ProductResponse();
        product.setId(1L); product.setName("Milk"); product.setPrice(new BigDecimal("5.90"));

        when(productClient.getProduct(1L)).thenReturn(product);
        doNothing().when(productClient).decrementStock(eq(1L), eq(2), any());

        Order savedOrder = new Order();
        savedOrder.setId(1L);
        savedOrder.setCustomerUsername("user");
        savedOrder.setStatus(OrderStatus.PENDING);
        savedOrder.setTotalPrice(new BigDecimal("11.80"));
        when(orderRepository.save(any())).thenReturn(savedOrder);

        OrderItemRequest item = new OrderItemRequest();
        item.setProductId(1L); item.setQuantity(2);

        Order result = orderService.createOrder(List.of(item), "user", "token");

        assertEquals("user", result.getCustomerUsername());
        verify(kafkaTemplate).send(eq("order-events"), any(OrderEvent.class));
    }

    @Test
    void findById_existing_returnsOrder() {
        Order order = new Order();
        order.setId(1L);
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        assertEquals(1L, orderService.findById(1L).getId());
    }

    @Test
    void findById_notFound_throws404() {
        when(orderRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(org.springframework.web.server.ResponseStatusException.class,
                () -> orderService.findById(99L));
    }

    @Test
    void updateStatus_validStatus_updatesAndPublishesEvent() {
        Order order = new Order();
        order.setId(1L);
        order.setStatus(OrderStatus.PENDING);
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(orderRepository.save(any())).thenReturn(order);

        orderService.updateStatus(1L, "SHIPPED");

        assertEquals(OrderStatus.SHIPPED, order.getStatus());
        verify(kafkaTemplate).send(eq("order-events"), any(OrderEvent.class));
    }
}
