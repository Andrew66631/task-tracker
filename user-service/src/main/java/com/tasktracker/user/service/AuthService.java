package com.tasktracker.user.service;

import com.tasktracker.user.dto.AuthResponse;
import com.tasktracker.user.dto.LoginRequest;
import com.tasktracker.user.dto.RegisterRequest;
import com.tasktracker.user.entity.User;
import com.tasktracker.user.entity.enums.UserRole;
import com.tasktracker.user.entity.enums.UserStatus;
import com.tasktracker.user.exception.UserAlreadyExistsException;
import com.tasktracker.user.kafka.UserEventPublisher;
import com.tasktracker.user.mapper.UserMapper;
import com.tasktracker.user.repository.UserRepository;
import com.tasktracker.user.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final UserEventPublisher userEventPublisher;   // ← НОВОЕ

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new UserAlreadyExistsException("Username already exists: " + request.getUsername());
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new UserAlreadyExistsException("Email already exists: " + request.getEmail());
        }

        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .fullName(request.getFullName())
                .role(UserRole.USER)
                .subRole(null)
                .status(UserStatus.PENDING)
                .build();

        User saved = userRepository.save(user);

        // ✅ Публикуем событие через outbox (в той же транзакции)
        userEventPublisher.publishUserCreated(saved);

        UserDetails userDetails = userDetailsService.loadUserByUsername(saved.getUsername());

        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", saved.getId().toString());
        claims.put("role", saved.getRole().name());

        String token = jwtService.generateToken(claims, userDetails);

        return AuthResponse.builder()
                .token(token)
                .user(UserMapper.INSTANCE.toResponse(saved))
                .build();
    }

    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());

        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getId().toString());
        claims.put("role", user.getRole().name());

        String token = jwtService.generateToken(claims, userDetails);

        return AuthResponse.builder()
                .token(token)
                .user(UserMapper.INSTANCE.toResponse(user))
                .build();
    }
}