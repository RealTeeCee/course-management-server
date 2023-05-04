package com.aptech.coursemanagementserver.services.authServices;

import java.io.IOException;

import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.aptech.coursemanagementserver.dtos.AuthenticationRequestDto;
import com.aptech.coursemanagementserver.dtos.AuthenticationResponseDto;
import com.aptech.coursemanagementserver.dtos.RegisterRequestDto;
import com.aptech.coursemanagementserver.enums.TokenType;
import com.aptech.coursemanagementserver.models.Token;
import com.aptech.coursemanagementserver.models.User;
import com.aptech.coursemanagementserver.repositories.TokenRepository;
import com.aptech.coursemanagementserver.repositories.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository repository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    // Register method create a user save it to db and generated token
    public AuthenticationResponseDto register(RegisterRequestDto request) {
        var user = User.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .build();
        var savedUser = repository.save(user);

        // Use JWTService to call generateToken() based on user above
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        saveUserToken(savedUser, jwtToken);
        return AuthenticationResponseDto.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    // Authenticate the user
    public AuthenticationResponseDto authenticate(AuthenticationRequestDto request) {
        // Authenticate the user with Username and Password.
        // If Authenticate fail -> Throw Exception -> kick out of this function
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()));

        // --> Authenticate Success
        var user = repository.findByEmail(request.getEmail())
                .orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        revokeAllUserTokens(user);
        saveUserToken(user, jwtToken);
        return AuthenticationResponseDto.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    // Save the token to database
    private void saveUserToken(User user, String jwtToken) {
        var token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }

    // Evict (revoke) back all tokens from user
    private void revokeAllUserTokens(User user) {
        var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }

    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String userEmail;
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return;
        }
        refreshToken = authHeader.substring(7);

        // Extract user email with the token in Authorization header
        userEmail = jwtService.extractUsername(refreshToken);
        if (userEmail != null) {
            // Find the User from the extracted email
            var user = this.repository.findByEmail(userEmail)
                    .orElseThrow();

            // Check if the token is from correct user and not expired
            if (jwtService.isTokenValid(refreshToken, user)) {
                // Generate accessToken with expired time (have no extra claims)
                var accessToken = jwtService.generateToken(user);
                // Remove last token from user
                revokeAllUserTokens(user);
                // Save new accessToken to database
                saveUserToken(user, accessToken);

                var authResponse = AuthenticationResponseDto.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build();
                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
            }
        }
    }
}