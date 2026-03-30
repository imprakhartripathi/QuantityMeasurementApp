package com.imprakhartripathi.qmaserver.quantitymeasurement.auth;

import com.imprakhartripathi.qmaserver.quantitymeasurement.model.AuthProvider;
import com.imprakhartripathi.qmaserver.quantitymeasurement.model.UserEntity;
import com.imprakhartripathi.qmaserver.quantitymeasurement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final AuthCookieUtil authCookieUtil;

    @Value("${app.oauth2.redirect-uri}")
    private String redirectUri;

    public OAuth2SuccessHandler(UserRepository userRepository, JwtService jwtService, AuthCookieUtil authCookieUtil) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.authCookieUtil = authCookieUtil;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {
        OAuth2User oauthUser = (OAuth2User) authentication.getPrincipal();
        String email = oauthUser.getAttribute("email");
        if (email == null || email.isBlank()) {
            throw new AuthFlowException("Google account does not have an email");
        }

        UserEntity user = userRepository.findByEmailIgnoreCase(email).orElseGet(() -> {
            UserEntity newUser = new UserEntity();
            newUser.setEmail(email);
            newUser.setName(safeString(oauthUser.getAttribute("name"), email));
            newUser.setPicture(safeString(oauthUser.getAttribute("picture"), null));
            newUser.setProvider(AuthProvider.GOOGLE);
            return newUser;
        });

        user.setName(safeString(oauthUser.getAttribute("name"), user.getName()));
        user.setPicture(safeString(oauthUser.getAttribute("picture"), user.getPicture()));
        if (user.getProvider() == null) {
            user.setProvider(AuthProvider.GOOGLE);
        }
        UserEntity savedUser = userRepository.save(user);
        JwtService.TokenPayload tokenPayload = jwtService.generateToken(savedUser);

        authCookieUtil.addAuthCookie(response, tokenPayload.token(), jwtService.getJwtExpirationMs(), request.isSecure());
        response.sendRedirect(redirectUri);
    }

    private String safeString(String value, String fallback) {
        if (value == null || value.isBlank()) {
            return fallback;
        }
        return value;
    }
}
