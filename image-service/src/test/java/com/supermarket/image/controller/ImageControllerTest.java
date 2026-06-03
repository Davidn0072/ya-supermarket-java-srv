package com.supermarket.image.controller;

import com.supermarket.image.service.ImageService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(
    value = ImageController.class,
    excludeAutoConfiguration = SecurityAutoConfiguration.class
)
class ImageControllerTest {

    @Autowired MockMvc mockMvc;
    @MockBean ImageService imageService;

    @Test
    void upload_validFile_returns200WithUrl() throws Exception {
        when(imageService.upload(any())).thenReturn("https://test-bucket.s3.amazonaws.com/uuid-milk.jpg");

        MockMultipartFile file = new MockMultipartFile(
                "file", "milk.jpg", "image/jpeg", "fake-image".getBytes());

        mockMvc.perform(multipart("/api/images/upload").file(file))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.url").value("https://test-bucket.s3.amazonaws.com/uuid-milk.jpg"));
    }
}
