package com.ecommerce.project.auth.service;

import com.ecommerce.project.auth.dto.*;
import com.ecommerce.project.auth.model.*;
import com.ecommerce.project.auth.repository.UserRepository;
import com.ecommerce.project.auth.security.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final EmailService emailService;

    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .roles(Set.of(Role.USER))
                .active(true)
                .build();

        userRepository.save(user);

        emailService.sendWelcomeEmail(
                user.getEmail(),
                user.getEmail()
        );

        return new AuthResponse(jwtProvider.generateToken(user));


    }

    public AuthResponse registerAdmin(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        User admin = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .roles(Set.of(Role.ADMIN))
                .active(true)
                .build();

        userRepository.save(admin);

        emailService.sendWelcomeEmail(
                admin.getEmail(),
                admin.getEmail()
        );

        return new AuthResponse(jwtProvider.generateToken(admin));


    }

    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        return new AuthResponse(jwtProvider.generateToken(user));


    }
}
