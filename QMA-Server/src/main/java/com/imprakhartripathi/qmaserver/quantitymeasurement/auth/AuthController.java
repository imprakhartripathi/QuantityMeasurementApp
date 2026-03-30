package com.imprakhartripathi.qmaserver.quantitymeasurement.auth;

import com.imprakhartripathi.qmaserver.quantitymeasurement.auth.dto.AuthResponse;
import com.imprakhartripathi.qmaserver.quantitymeasurement.auth.dto.LoginRequest;
import com.imprakhartripathi.qmaserver.quantitymeasurement.auth.dto.SignupRequest;
import com.imprakhartripathi.qmaserver.quantitymeasurement.auth.dto.UserProfileResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final AuthService authService;
    private final AuthCookieUtil authCookieUtil;
    private final JwtService jwtService;

    public AuthController(AuthService authService, AuthCookieUtil authCookieUtil, JwtService jwtService) {
        this.authService = authService;
        this.authCookieUtil = authCookieUtil;
        this.jwtService = jwtService;
    }

    @PostMapping("/signup")
    public AuthResponse signup(@Valid @RequestBody SignupRequest request, HttpServletRequest httpServletRequest,
                               HttpServletResponse httpServletResponse) {
        AuthResponse authResponse = authService.signup(request);
        authCookieUtil.addAuthCookie(httpServletResponse, authResponse.getAccessToken(),
                jwtService.getJwtExpirationMs(), httpServletRequest.isSecure());
        return authResponse;
    }

    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody LoginRequest request, HttpServletRequest httpServletRequest,
                              HttpServletResponse httpServletResponse) {
        AuthResponse authResponse = authService.login(request);
        authCookieUtil.addAuthCookie(httpServletResponse, authResponse.getAccessToken(),
                jwtService.getJwtExpirationMs(), httpServletRequest.isSecure());
        return authResponse;
    }

    @GetMapping("/session")
    public ResponseEntity<UserProfileResponse> session(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()
                || "anonymousUser".equalsIgnoreCase(authentication.getName())) {
            return ResponseEntity.noContent().build();
        }

        String email = extractEmail(authentication);
        if (email == null || email.isBlank()) {
            return ResponseEntity.noContent().build();
        }

        try {
            return ResponseEntity.ok(authService.getProfileByEmail(email));
        } catch (AuthFlowException ignored) {
            return ResponseEntity.noContent().build();
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                                       Authentication authentication) {
        new SecurityContextLogoutHandler().logout(httpServletRequest, httpServletResponse, authentication);
        authCookieUtil.clearAuthCookie(httpServletResponse, httpServletRequest.isSecure());
        httpServletResponse.setHeader("Clear-Site-Data", "\"cache\", \"cookies\", \"storage\"");
        return ResponseEntity.noContent().build();
    }

    private String extractEmail(Authentication authentication) {
        Object principal = authentication.getPrincipal();

        if (principal instanceof UserDetails userDetails) {
            return userDetails.getUsername();
        }

        if (principal instanceof OAuth2User oauth2User) {
            Object email = oauth2User.getAttribute("email");
            if (email instanceof String emailString && !emailString.isBlank()) {
                return emailString;
            }
        }

        String authName = authentication.getName();
        if (authName != null && authName.contains("@")) {
            return authName;
        }

        return null;
    }
}
