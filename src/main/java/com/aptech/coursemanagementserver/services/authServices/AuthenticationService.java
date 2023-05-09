package com.aptech.coursemanagementserver.services.authServices;

import static com.aptech.coursemanagementserver.constants.GlobalStorage.TOKEN_PREFIX;

import java.io.IOException;
import java.text.ParseException;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.aptech.coursemanagementserver.dtos.AuthenticationRequestDto;
import com.aptech.coursemanagementserver.dtos.AuthenticationResponseDto;
import com.aptech.coursemanagementserver.dtos.RegisterRequestDto;
import com.aptech.coursemanagementserver.enums.AntType;
import com.aptech.coursemanagementserver.enums.TokenType;
import com.aptech.coursemanagementserver.exceptions.InvalidTokenException;
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
    private final ModelMapper modelMapper;
    private final UserRepository repository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponseDto login(RegisterRequestDto request) {
        var user = repository.findByEmail(request.getEmail()).get();
        if (user != null) {
            // Check if BCrypt of request MATCHES BCrypt of user (Compare hash)
            Boolean isPwdMatch = passwordEncoder.matches(request.getPassword(), user.getPassword());
            if (isPwdMatch) {
                var jwtToken = jwtService.generateToken(user);
                var refreshToken = jwtService.generateRefreshToken(user);
                saveUserToken(user, jwtToken);
                return AuthenticationResponseDto.builder()
                        .accessToken(jwtToken)
                        .refreshToken(refreshToken)
                        .type(AntType.success)
                        .message("HEADER_STRING")
                        .build();
            }

        }

        return AuthenticationResponseDto.builder()
                .type(AntType.error).message("HEADER_STRING")
                .build();
    }

    // Register method create a user save it to db and generated token
    public User register(RegisterRequestDto request) {

        // var user = User.builder()
        // .first_name(request.getFirstname())
        // .last_name(request.getLastname())
        // .email(request.getEmail())
        // .password(passwordEncoder.encode(request.getPassword()))
        // .role(request.getRole())
        // .build();
        // var savedUser = repository.save(user);
        request.setPassword(passwordEncoder.encode(request.getPassword()));
        var savedUser = repository.save(modelMapper.map(request, User.class));

        // 1. Send Email
        // 2. Customer click on Email. Link URL verify. Ex:
        // .../verify?email=MASKDLSADJASDKLSAJDASLDJK
        // 3. Redirect to React actived mail page. (Button verify --> user click this
        // button --> call API actived at Bakend).
        // 4. Get response from backend. Base on status. If error => show error message.
        // If success => Redirect to login page.
        // 5. Login flow as usual. (username, password and verify = true => Create
        // token)
        // Decrypt email in link above. SELECT email FROM User WHERE isVerify == 0. Set
        // isVerfy = 1 -> Redirect to Login
        // Input username , password. check username, password where isVerify == 1.
        // Login success -> generate token
        // isVerify == 0 -> Login fail, email must verify

        return savedUser;
    }

    // Use JWTService to call generateToken() based on user above
    public AuthenticationResponseDto generateTokenWithoutVerify(User user) {
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        saveUserToken(user, jwtToken);
        return AuthenticationResponseDto.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    // Use JWTService to call generateToken() based on user above
    public String verifyEmailRegister(String token) throws ParseException {
        Token t = tokenRepository.findByToken(token).get();
        if (t == null)
            throw new InvalidTokenException(HttpStatus.UNAUTHORIZED, "Invalid Token");
        t.getUser().setVerified(true);
        tokenRepository.save(t);

        return "Your Email Has Been Verified";
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
                .token_type(TokenType.BEARER)
                .isExpired(false)
                .isRevoked(false)
                .build();
        tokenRepository.save(token);
    }

    public void saveUserVerificationToken(User user, String jwtToken) {
        var token = Token.builder()
                .user(user)
                .token(jwtToken)
                .token_type(TokenType.VERIFY)
                .isExpired(false)
                .isRevoked(false)
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
        if (authHeader == null || !authHeader.startsWith(TOKEN_PREFIX)) {
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