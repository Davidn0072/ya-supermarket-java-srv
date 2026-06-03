package com.supermarket.gateway.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class JwtServiceTest {

    @InjectMocks
    private JwtService jwtService;

    private static final String SECRET = "supermarket-jwt-secret-key-must-be-at-least-32-characters";

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(jwtService, "secret", SECRET);
    }

    private String generateToken(String username, long expirationMs) {
        SecretKey key = Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));
        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expirationMs))
                .signWith(key)
                .compact();
    }

    @Test
    void isTokenValid_validToken_returnsTrue() {
        String token = generateToken("user", 86400000L);
        assertTrue(jwtService.isTokenValid(token));
    }

    @Test
    void isTokenValid_expiredToken_returnsFalse() {
        String token = generateToken("user", -1000L);
        assertFalse(jwtService.isTokenValid(token));
    }

    @Test
    void isTokenValid_invalidToken_returnsFalse() {
        assertFalse(jwtService.isTokenValid("not.a.token"));
    }

    @Test
    void extractUsername_validToken_returnsUsername() {
        String token = generateToken("testuser", 86400000L);
        assertEquals("testuser", jwtService.extractUsername(token));
    }
}
