package com.xelvo.companion.auth.service;

import com.xelvo.companion.auth.dto.*;
import com.xelvo.companion.common.exception.BusinessException;
import com.xelvo.companion.common.exception.DuplicateResourceException;
import com.xelvo.companion.companion.entity.CompanionProfile;
import com.xelvo.companion.companion.repository.CompanionRepository;
import com.xelvo.companion.security.CurrentUserService;
import com.xelvo.companion.security.JwtService;
import com.xelvo.companion.user.entity.*;
import com.xelvo.companion.user.repository.UserRepository;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final CompanionRepository companionRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final CurrentUserService currentUserService;

    public AuthService(UserRepository userRepository,
                       CompanionRepository companionRepository,
                       PasswordEncoder passwordEncoder,
                       AuthenticationManager authenticationManager,
                       JwtService jwtService,
                       CurrentUserService currentUserService) {
        this.userRepository = userRepository;
        this.companionRepository = companionRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.currentUserService = currentUserService;
    }

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmailIgnoreCase(request.email())) {
            throw new DuplicateResourceException("Email is already registered");
        }

        if (request.role() == Role.ADMIN) {
            throw new BusinessException("Admin accounts cannot be self-registered");
        }

        User user = new User(
                request.fullName().trim(),
                request.email().trim().toLowerCase(),
                passwordEncoder.encode(request.password()),
                request.role()
        );

        user = userRepository.save(user);

        if (request.role() == Role.COMPANION) {
            CompanionProfile profile = new CompanionProfile(
                    user,
                    user.getFullName(),
                    25,
                    "Delhi",
                    "Not specified",
                    "",
                    new BigDecimal("600.00")
            );
            companionRepository.save(profile);
        }

        return createAuthResponse(user);
    }

    public AuthResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password())
        );

        User user = userRepository.findByEmailIgnoreCase(authentication.getName())
                .orElseThrow(() -> new BusinessException("User account was not found"));

        if (user.getStatus() != UserStatus.ACTIVE) {
            throw new BusinessException("User account is not active");
        }

        return createAuthResponse(user);
    }

    @Transactional(readOnly = true)
    public MeResponse me() {
        User user = currentUserService.get();
        return new MeResponse(
                user.getId(),
                user.getFullName(),
                user.getEmail(),
                user.getRole().name(),
                user.getStatus().name(),
                user.getCity()
        );
    }

    private AuthResponse createAuthResponse(User user) {
        String token = jwtService.generateToken(
                user.getEmail(),
                user.getRole().name(),
                user.getId()
        );

        return new AuthResponse(token, user.getId(), user.getFullName(), user.getEmail(), user.getRole().name());
    }
}
