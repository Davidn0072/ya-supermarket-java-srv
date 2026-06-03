package com.supermarket.search.controller;

import com.supermarket.search.document.ProductDocument;
import com.supermarket.search.repository.ProductSearchRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(
    value = SearchController.class,
    excludeAutoConfiguration = SecurityAutoConfiguration.class
)
class SearchControllerTest {

    @Autowired MockMvc mockMvc;
    @MockBean ProductSearchRepository searchRepository;

    @Test
    void search_returnsMatchingProducts() throws Exception {
        ProductDocument doc = new ProductDocument();
        doc.setId("1");
        doc.setName("Milk");

        when(searchRepository.findByNameContainingOrDescriptionContaining("milk", "milk"))
                .thenReturn(List.of(doc));

        mockMvc.perform(get("/api/search/products").param("q", "milk"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Milk"));
    }

    @Test
    void search_noResults_returnsEmptyList() throws Exception {
        when(searchRepository.findByNameContainingOrDescriptionContaining("xyz", "xyz"))
                .thenReturn(List.of());

        mockMvc.perform(get("/api/search/products").param("q", "xyz"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }
}
