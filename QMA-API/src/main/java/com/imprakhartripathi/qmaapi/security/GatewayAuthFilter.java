package com.imprakhartripathi.qmaapi.security;

import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.Optional;

@Component
public class GatewayAuthFilter implements GlobalFilter, Ordered {
    private static final String AUTH_COOKIE_NAME = "QMA_AUTH_TOKEN";
    private static final String USER_EMAIL_HEADER = "X-User-Email";

    private final GatewayJwtService gatewayJwtService;

    public GatewayAuthFilter(GatewayJwtService gatewayJwtService) {
        this.gatewayJwtService = gatewayJwtService;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getURI().getPath();

        if (!requiresAuthentication(path, exchange.getRequest().getMethod())) {
            return chain.filter(exchange);
        }

        String token = resolveToken(exchange.getRequest());
        if (token == null || token.isBlank()) {
            return unauthorized(exchange.getResponse(), "Missing authentication token");
        }

        Optional<String> email = gatewayJwtService.extractEmailIfValid(token);
        if (email.isEmpty()) {
            return unauthorized(exchange.getResponse(), "Invalid or expired authentication token");
        }

        ServerHttpRequest request = exchange.getRequest().mutate()
                .headers(headers -> headers.set(USER_EMAIL_HEADER, email.get()))
                .build();

        return chain.filter(exchange.mutate().request(request).build());
    }

    @Override
    public int getOrder() {
        return -100;
    }

    private boolean requiresAuthentication(String path, HttpMethod method) {
        if (HttpMethod.OPTIONS.equals(method)) {
            return false;
        }
        return path.startsWith("/api/v1/quantities/")
                || path.startsWith("/api/v1/users/");
    }

    private String resolveToken(ServerHttpRequest request) {
        String authorization = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (authorization != null && authorization.startsWith("Bearer ")) {
            return authorization.substring(7).trim();
        }

        HttpCookie authCookie = request.getCookies().getFirst(AUTH_COOKIE_NAME);
        if (authCookie != null) {
            return authCookie.getValue();
        }
        return null;
    }

    private Mono<Void> unauthorized(ServerHttpResponse response, String message) {
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        byte[] body = ("{\"message\":\"" + message + "\"}").getBytes(StandardCharsets.UTF_8);
        DataBuffer dataBuffer = response.bufferFactory().wrap(body);
        return response.writeWith(Mono.just(dataBuffer));
    }
}
