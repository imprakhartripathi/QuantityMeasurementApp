package com.imprakhartripathi.qmaserver.quantitymeasurement.auth;

import com.imprakhartripathi.qmaserver.quantitymeasurement.auth.dto.UpdateUserProfileRequest;
import com.imprakhartripathi.qmaserver.quantitymeasurement.auth.dto.UserProfileResponse;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
        return authService.getProfileByEmail(resolveEmail(authentication));
    }

    @PatchMapping("/me")
    public UserProfileResponse updateMyProfile(Authentication authentication,
                                               @Valid @RequestBody UpdateUserProfileRequest request) {
        return authService.updateProfile(resolveEmail(authentication), request);
    }

    private String resolveEmail(Authentication authentication) {
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

        throw new AuthFlowException("Unable to resolve user email from authentication");
    }
}
