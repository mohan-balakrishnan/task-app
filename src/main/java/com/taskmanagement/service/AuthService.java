package com.taskmanagement.service;

import com.taskmanagement.dto.JwtResponse;
import com.taskmanagement.dto.LoginRequest;
import com.taskmanagement.dto.UserResponse;
import com.taskmanagement.entity.User;
import com.taskmanagement.repository.UserRepository;
import com.taskmanagement.security.JwtUtils;
import com.taskmanagement.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    @Transactional
    public JwtResponse authenticateUser(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                loginRequest.getUsername(),
                loginRequest.getPassword()
            )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();
        String jwt = jwtUtils.generateToken(userPrincipal.getUsername());
        String refreshToken = jwtUtils.generateRefreshToken(userPrincipal.getUsername());

        User user = userRepository.findByUsername(userPrincipal.getUsername())
            .orElseThrow(() -> new RuntimeException("User not found"));

        log.info("User authenticated successfully: {}", userPrincipal.getUsername());

        return JwtResponse.builder()
            .accessToken(jwt)
            .refreshToken(refreshToken)
            .tokenType("Bearer")
            .expiresIn(jwtUtils.getExpirationTime())
            .user(UserResponse.fromEntity(user))
            .build();
    }

    @Transactional
    public User registerUser(String username, String email, String password, 
                           String firstName, String lastName, User.Role role) {
        if (userRepository.existsByUsername(username)) {
            throw new RuntimeException("Username is already taken: " + username);
        }

        if (userRepository.existsByEmail(email)) {
            throw new RuntimeException("Email is already in use: " + email);
        }

        User user = User.builder()
            .username(username)
            .email(email)
            .password(passwordEncoder.encode(password))
            .firstName(firstName)
            .lastName(lastName)
            .role(role != null ? role : User.Role.USER)
            .enabled(true)
            .build();

        User savedUser = userRepository.save(user);
        log.info("User registered successfully: {}", username);

        return savedUser;
    }

    public JwtResponse refreshToken(String refreshToken) {
        if (!jwtUtils.validateToken(refreshToken)) {
            throw new RuntimeException("Refresh token is not valid!");
        }

        String username = jwtUtils.getUsernameFromToken(refreshToken);
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("User not found"));

        String newAccessToken = jwtUtils.generateToken(username);
        String newRefreshToken = jwtUtils.generateRefreshToken(username);

        log.info("Token refreshed for user: {}", username);

        return JwtResponse.builder()
            .accessToken(newAccessToken)
            .refreshToken(newRefreshToken)
            .tokenType("Bearer")
            .expiresIn(jwtUtils.getExpirationTime())
            .user(UserResponse.fromEntity(user))
            .build();
    }
}