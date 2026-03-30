package com.imprakhartripathi.qmaserver.quantitymeasurement.auth.dto;

import java.time.Instant;

public class AuthResponse {
    private String tokenType;
    private String accessToken;
    private long issuedAtEpochSeconds;
    private long expiresAtEpochSeconds;
    private UserProfileResponse user;

    public AuthResponse(String accessToken, Instant issuedAt, Instant expiresAt, UserProfileResponse user) {
        this.tokenType = "Bearer";
        this.accessToken = accessToken;
        this.issuedAtEpochSeconds = issuedAt.getEpochSecond();
        this.expiresAtEpochSeconds = expiresAt.getEpochSecond();
        this.user = user;
    }

    public String getTokenType() {
        return tokenType;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public long getIssuedAtEpochSeconds() {
        return issuedAtEpochSeconds;
    }

    public long getExpiresAtEpochSeconds() {
        return expiresAtEpochSeconds;
    }

    public UserProfileResponse getUser() {
        return user;
    }
}
