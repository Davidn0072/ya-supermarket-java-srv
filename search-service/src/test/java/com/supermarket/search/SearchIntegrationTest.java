package com.supermarket.search;

import com.supermarket.search.document.ProductDocument;
import com.supermarket.search.repository.ProductSearchRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class SearchIntegrationTest {

    @Autowired MockMvc mockMvc;
    @MockBean ProductSearchRepository searchRepository;

    @Test
    void searchEndpoint_isPublic_noAuthRequired() throws Exception {
        when(searchRepository.findByNameContainingOrDescriptionContaining("milk", "milk"))
                .thenReturn(List.of());

        mockMvc.perform(get("/api/search/products").param("q", "milk"))
                .andExpect(status().isOk());
    }

    @Test
    void searchEndpoint_returnsResults() throws Exception {
        ProductDocument doc = new ProductDocument();
        doc.setId("1"); doc.setName("Milk");

        when(searchRepository.findByNameContainingOrDescriptionContaining("milk", "milk"))
                .thenReturn(List.of(doc));

        mockMvc.perform(get("/api/search/products").param("q", "milk"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Milk"));
    }
}
