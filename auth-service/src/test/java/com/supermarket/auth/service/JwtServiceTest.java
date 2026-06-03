package com.supermarket.auth.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class JwtServiceTest {

    @InjectMocks
    private JwtService jwtService;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(jwtService, "secret",
                "supermarket-jwt-secret-key-must-be-at-least-32-characters");
        ReflectionTestUtils.setField(jwtService, "expiration", 86400000L);
    }

    private UserDetails testUser() {
        return User.withUsername("testuser").password("pass").roles("USER").build();
    }

    @Test
    void generateToken_returnsNonNullToken() {
        String token = jwtService.generateToken(testUser());
        assertNotNull(token);
        assertFalse(token.isBlank());
    }

    @Test
    void extractUsername_returnsCorrectUsername() {
        String token = jwtService.generateToken(testUser());
        assertEquals("testuser", jwtService.extractUsername(token));
    }

    @Test
    void isTokenValid_returnsTrueForValidToken() {
        UserDetails user = testUser();
        String token = jwtService.generateToken(user);
        assertTrue(jwtService.isTokenValid(token, user));
    }

    @Test
    void isTokenValid_returnsFalseForWrongUser() {
        String token = jwtService.generateToken(testUser());
        UserDetails otherUser = User.withUsername("other").password("pass").roles("USER").build();
        assertFalse(jwtService.isTokenValid(token, otherUser));
    }
}
