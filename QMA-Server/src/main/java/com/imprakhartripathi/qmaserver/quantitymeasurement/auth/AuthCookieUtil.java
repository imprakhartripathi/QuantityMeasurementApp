package com.imprakhartripathi.qmaserver.quantitymeasurement.auth;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

@Component
public class AuthCookieUtil {
    public static final String AUTH_COOKIE_NAME = "QMA_AUTH_TOKEN";

    public void addAuthCookie(HttpServletResponse response, String token, long maxAgeMs, boolean secure) {
        ResponseCookie authCookie = ResponseCookie.from(AUTH_COOKIE_NAME, token)
                .httpOnly(true)
                .secure(secure)
                .sameSite("Lax")
                .path("/")
                .maxAge(Math.max(0, maxAgeMs / 1000))
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, authCookie.toString());
    }

    public void clearAuthCookie(HttpServletResponse response, boolean secure) {
        ResponseCookie clearCookie = ResponseCookie.from(AUTH_COOKIE_NAME, "")
                .httpOnly(true)
                .secure(secure)
                .sameSite("Lax")
                .path("/")
                .maxAge(0)
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, clearCookie.toString());
    }
}
