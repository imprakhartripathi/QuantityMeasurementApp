package com.imprakhartripathi.qmaserver.quantitymeasurement.auth;

import com.imprakhartripathi.qmaserver.quantitymeasurement.auth.dto.UserProfileResponse;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    private final AuthService authService;

    public UserController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/me")
    public UserProfileResponse myProfile(Authentication authentication) {
        return authService.getProfileByEmail(authentication.getName());
    }
}
