package com.supermarket.gateway.filter;

import com.supermarket.gateway.service.JwtService;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class JwtGatewayFilter implements GlobalFilter, Ordered {

    private final JwtService jwtService;

    private static final List<String> PUBLIC_POST_PATHS = List.of(
            "/api/auth/login",
            "/api/auth/logout"
    );

    public JwtGatewayFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getPath().value();
        HttpMethod method = request.getMethod();

        if (isPublic(path, method)) {
            return chain.filter(exchange);
        }

        String token = extractTokenFromCookie(request);
        if (token == null || !jwtService.isTokenValid(token)) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        return chain.filter(exchange);
    }

    private boolean isPublic(String path, HttpMethod method) {
        if (PUBLIC_POST_PATHS.contains(path)) return true;
        if (HttpMethod.GET.equals(method)) {
            return path.startsWith("/api/products")
                    || path.startsWith("/api/categories")
                    || path.startsWith("/api/search");
        }
        return false;
    }

    private String extractTokenFromCookie(ServerHttpRequest request) {
        List<String> cookies = request.getHeaders().get("Cookie");
        if (cookies == null) return null;
        return cookies.stream()
                .flatMap(c -> java.util.Arrays.stream(c.split(";")))
                .map(String::trim)
                .filter(c -> c.startsWith("ACCESS_TOKEN="))
                .map(c -> c.substring("ACCESS_TOKEN=".length()))
                .findFirst()
                .orElse(null);
    }

    @Override
    public int getOrder() {
        return -1;
    }
}
