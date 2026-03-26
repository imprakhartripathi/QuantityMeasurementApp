package com.imprakhartripathi.qmaserver.quantitymeasurement.auth;

import com.imprakhartripathi.qmaserver.quantitymeasurement.auth.dto.UpdateUserProfileRequest;
import com.imprakhartripathi.qmaserver.quantitymeasurement.auth.dto.UserProfileResponse;
import com.imprakhartripathi.qmaserver.quantitymeasurement.model.QuantityMeasurementDTO;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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

    @PatchMapping("/me")
    public UserProfileResponse updateMyProfile(Authentication authentication,
                                               @Valid @RequestBody UpdateUserProfileRequest request) {
        return authService.updateProfile(authentication.getName(), request);
    }

    @GetMapping("/me/history")
    public List<QuantityMeasurementDTO> myHistory(Authentication authentication) {
        return authService.getHistoryByEmail(authentication.getName());
    }
}
