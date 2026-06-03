package com.supermarket.gateway;

import com.supermarket.gateway.service.JwtService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class GatewayIntegrationTest {

    @Autowired
    JwtService jwtService;

    private static final String SECRET = "supermarket-jwt-secret-key-must-be-at-least-32-characters";

    private String generateToken(String username) {
        SecretKey key = Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));
        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 86400000L))
                .signWith(key)
                .compact();
    }

    @Test
    void jwtService_isLoaded() {
        assertNotNull(jwtService);
    }

    @Test
    void validToken_isAccepted() {
        String token = generateToken("user");
        assertTrue(jwtService.isTokenValid(token));
    }

    @Test
    void invalidToken_isRejected() {
        assertFalse(jwtService.isTokenValid("invalid.token.here"));
    }
}
