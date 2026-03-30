package com.imprakhartripathi.qmaserver.quantitymeasurement.auth;

import com.imprakhartripathi.qmaserver.quantitymeasurement.auth.dto.AuthResponse;
import com.imprakhartripathi.qmaserver.quantitymeasurement.auth.dto.LoginRequest;
import com.imprakhartripathi.qmaserver.quantitymeasurement.auth.dto.SignupRequest;
import com.imprakhartripathi.qmaserver.quantitymeasurement.auth.dto.UpdateUserProfileRequest;
import com.imprakhartripathi.qmaserver.quantitymeasurement.auth.dto.UserProfileResponse;
import com.imprakhartripathi.qmaserver.quantitymeasurement.model.AuthProvider;
import com.imprakhartripathi.qmaserver.quantitymeasurement.model.UserEntity;
import com.imprakhartripathi.qmaserver.quantitymeasurement.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @Transactional
    public AuthResponse signup(SignupRequest request) {
        String email = request.getEmail().trim().toLowerCase();
        if (userRepository.existsByEmailIgnoreCase(email)) {
            throw new AuthFlowException("User already exists with email: " + email);
        }

        UserEntity user = new UserEntity();
        user.setName(request.getName());
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setPicture(request.getPicture());
        user.setProvider(AuthProvider.LOCAL);
        UserEntity savedUser = userRepository.save(user);
        JwtService.TokenPayload tokenPayload = jwtService.generateToken(savedUser);
        return new AuthResponse(tokenPayload.token(), tokenPayload.issuedAt(), tokenPayload.expiresAt(),
                UserProfileResponse.fromUser(savedUser));
    }

    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest request) {
        String email = request.getEmail().trim().toLowerCase();
        UserEntity user = userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new AuthFlowException("Invalid email or password"));

        if (user.getProvider() == AuthProvider.GOOGLE) {
            throw new AuthFlowException("This account uses Google login. Continue with Google OAuth.");
        }
        if (user.getPassword() == null || !passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new AuthFlowException("Invalid email or password");
        }

        JwtService.TokenPayload tokenPayload = jwtService.generateToken(user);
        return new AuthResponse(tokenPayload.token(), tokenPayload.issuedAt(), tokenPayload.expiresAt(),
                UserProfileResponse.fromUser(user));
    }

    @Transactional(readOnly = true)
    public UserProfileResponse getProfileByEmail(String email) {
        UserEntity user = userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new AuthFlowException("User not found"));
        return UserProfileResponse.fromUser(user);
    }

    @Transactional(readOnly = true)
    public UserEntity getUserByEmail(String email) {
        return userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new AuthFlowException("User not found"));
    }

    @Transactional
    public UserProfileResponse updateProfile(String email, UpdateUserProfileRequest request) {
        UserEntity user = getUserByEmail(email);
        user.setName(request.getName().trim());
        user.setPicture(normalizePicture(request.getPicture()));
        UserEntity updatedUser = userRepository.save(user);
        return UserProfileResponse.fromUser(updatedUser);
    }

    private String normalizePicture(String picture) {
        if (picture == null || picture.isBlank()) {
            return null;
        }
        return picture.trim();
    }
}
