package com.imprakhartripathi.qmaapi.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

@Service
public class GatewayJwtService {

    @Value("${app.jwt.secret:${QMA_JWT_SECRET:change-me-to-a-long-random-secret-key-at-least-32-bytes}}")
    private String jwtSecret;

    private SecretKey secretKey;

    @PostConstruct
    void init() {
        this.secretKey = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }

    public Optional<String> extractEmailIfValid(String token) {
        try {
            Jws<Claims> signedClaims = Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token);
            Claims claims = signedClaims.getPayload();
            String subject = claims.getSubject();
            if (subject == null || subject.isBlank()) {
                return Optional.empty();
            }
            return Optional.of(subject.trim().toLowerCase());
        } catch (Exception ignored) {
            return Optional.empty();
        }
    }
}
