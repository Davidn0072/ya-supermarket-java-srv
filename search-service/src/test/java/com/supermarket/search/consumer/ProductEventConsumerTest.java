package com.supermarket.search.consumer;

import com.supermarket.search.document.ProductDocument;
import com.supermarket.search.dto.ProductEvent;
import com.supermarket.search.repository.ProductSearchRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductEventConsumerTest {

    @Mock ProductSearchRepository searchRepository;
    @InjectMocks ProductEventConsumer consumer;

    @Test
    void handle_createEvent_savesDocument() {
        ProductEvent event = new ProductEvent();
        event.setEventType("CREATE");
        event.setProductId(1L);
        event.setName("Milk");
        event.setDescription("Fresh milk");
        event.setPrice(new BigDecimal("5.90"));
        event.setCategoryName("Dairy");

        consumer.handle(event);

        ArgumentCaptor<ProductDocument> captor = ArgumentCaptor.forClass(ProductDocument.class);
        verify(searchRepository).save(captor.capture());
        assertEquals("1", captor.getValue().getId());
        assertEquals("Milk", captor.getValue().getName());
    }

    @Test
    void handle_updateEvent_updatesDocument() {
        ProductEvent event = new ProductEvent();
        event.setEventType("UPDATE");
        event.setProductId(1L);
        event.setName("Milk Updated");
        event.setPrice(new BigDecimal("6.00"));

        consumer.handle(event);

        verify(searchRepository).save(any(ProductDocument.class));
    }

    @Test
    void handle_deleteEvent_deletesDocument() {
        ProductEvent event = new ProductEvent();
        event.setEventType("DELETE");
        event.setProductId(1L);

        consumer.handle(event);

        verify(searchRepository).deleteById("1");
        verify(searchRepository, never()).save(any());
    }
}
