package com.supermarket.image.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ImageServiceTest {

    @Mock S3Client s3Client;
    @InjectMocks ImageService imageService;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(imageService, "bucketName", "test-bucket");
        ReflectionTestUtils.setField(imageService, "region", "us-east-1");
    }

    @Test
    void upload_validFile_returnsS3Url() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "file", "milk.jpg", "image/jpeg", "fake-image".getBytes());

        when(s3Client.putObject(any(PutObjectRequest.class), any(software.amazon.awssdk.core.sync.RequestBody.class)))
                .thenReturn(PutObjectResponse.builder().build());

        String url = imageService.upload(file);

        assertNotNull(url);
        assertTrue(url.contains("test-bucket"));
        assertTrue(url.contains("milk.jpg"));
        verify(s3Client).putObject(any(PutObjectRequest.class), any(software.amazon.awssdk.core.sync.RequestBody.class));
    }

    @Test
    void upload_generatesUniqueKeys() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "file", "product.png", "image/png", "data".getBytes());

        when(s3Client.putObject(any(PutObjectRequest.class), any(software.amazon.awssdk.core.sync.RequestBody.class)))
                .thenReturn(PutObjectResponse.builder().build());

        String url1 = imageService.upload(file);
        String url2 = imageService.upload(file);

        assertNotEquals(url1, url2);
    }
}
