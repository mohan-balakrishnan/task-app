package com.taskmanagement.controller;

import com.taskmanagement.dto.JwtResponse;
import com.taskmanagement.dto.LoginRequest;
import com.taskmanagement.dto.UserResponse;
import com.taskmanagement.entity.User;
import com.taskmanagement.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Authentication and registration endpoints")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signin")
    @Operation(summary = "User login", description = "Authenticate user and return JWT token")
    public ResponseEntity<JwtResponse> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        JwtResponse jwtResponse = authService.authenticateUser(loginRequest);
        return ResponseEntity.ok(jwtResponse);
    }

    @PostMapping("/signup")
    @Operation(summary = "User registration", description = "Register a new user account")
    public ResponseEntity<UserResponse> registerUser(@RequestBody Map<String, String> signUpRequest) {
        String username = signUpRequest.get("username");
        String email = signUpRequest.get("email");
        String password = signUpRequest.get("password");
        String firstName = signUpRequest.get("firstName");
        String lastName = signUpRequest.get("lastName");
        String roleStr = signUpRequest.get("role");

        User.Role role = User.Role.USER;
        if (roleStr != null && !roleStr.isEmpty()) {
            try {
                role = User.Role.valueOf(roleStr.toUpperCase());
            } catch (IllegalArgumentException e) {
                role = User.Role.USER;
            }
        }

        User user = authService.registerUser(username, email, password, firstName, lastName, role);
        return ResponseEntity.ok(UserResponse.fromEntity(user));
    }

    @PostMapping("/refresh")
    @Operation(summary = "Refresh token", description = "Refresh JWT access token using refresh token")
    public ResponseEntity<JwtResponse> refreshToken(@RequestBody Map<String, String> refreshRequest) {
        String refreshToken = refreshRequest.get("refreshToken");
        if (refreshToken == null || refreshToken.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        JwtResponse jwtResponse = authService.refreshToken(refreshToken);
        return ResponseEntity.ok(jwtResponse);
    }
}