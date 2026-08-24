package com.securevault.service;

import com.securevault.dto.AuthResponse;
import com.securevault.dto.LoginRequest;
import com.securevault.dto.RegisterRequest;
import com.securevault.entity.User;
import com.securevault.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuditLogService auditLogService;

    @InjectMocks
    private AuthService authService;

    @Test
    void registerShouldCreateUser() {

        RegisterRequest request = new RegisterRequest();
        request.setName("Test User");
        request.setEmail("test@gmail.com");
        request.setPassword("password123");

        when(userRepository.findByEmail("test@gmail.com"))
                .thenReturn(Optional.empty());

        when(passwordEncoder.encode("password123"))
                .thenReturn("encryptedPassword");

        String result = authService.register(request);

        assertEquals("User registered successfully", result);

        verify(userRepository).save(any(User.class));
        verify(passwordEncoder).encode("password123");
    }
    @Test
    void registerShouldRejectDuplicateEmail() {

        RegisterRequest request = new RegisterRequest();
        request.setName("Test User");
        request.setEmail("test@gmail.com");
        request.setPassword("password123");

        User existingUser = new User();
        existingUser.setEmail("test@gmail.com");

        when(userRepository.findByEmail("test@gmail.com"))
                .thenReturn(Optional.of(existingUser));

        String result = authService.register(request);

        assertEquals("Email is already registered", result);

        verify(userRepository, never()).save(any(User.class));
    }
    @Test
    void loginShouldReturnTokensForValidCredentials() {

        LoginRequest request = new LoginRequest();
        request.setEmail("test@gmail.com");
        request.setPassword("password123");

        User user = new User();
        user.setEmail("test@gmail.com");
        user.setPassword("encryptedPassword");

        when(userRepository.findByEmail("test@gmail.com"))
                .thenReturn(Optional.of(user));

        when(passwordEncoder.matches("password123", "encryptedPassword"))
                .thenReturn(true);

        when(jwtService.generateAccessToken(user))
                .thenReturn("access-token");

        when(jwtService.generateRefreshToken("test@gmail.com"))
                .thenReturn("refresh-token");

        AuthResponse response = authService.login(request);

        assertEquals("access-token", response.getAccessToken());
        assertEquals("refresh-token", response.getRefreshToken());

        verify(auditLogService).log("LOGIN_SUCCESS", user);
    }
    @Test
    void loginShouldFailIfUserDoesNotExist() {

        LoginRequest request = new LoginRequest();
        request.setEmail("unknown@gmail.com");
        request.setPassword("password123");

        when(userRepository.findByEmail("unknown@gmail.com"))
                .thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> authService.login(request)
        );

        assertEquals("Invalid email or password", exception.getMessage());

        verifyNoInteractions(auditLogService);
    }
    @Test
    void refreshAccessTokenShouldGenerateNewToken() {

        String refreshToken = "refresh-token";
        String email = "test@gmail.com";

        User user = new User();
        user.setEmail(email);

        when(jwtService.extractEmail(refreshToken))
                .thenReturn(email);

        when(userRepository.findByEmail(email))
                .thenReturn(Optional.of(user));

        when(jwtService.generateAccessToken(user))
                .thenReturn("new-access-token");

        String result = authService.refreshAccessToken(refreshToken);

        assertEquals("new-access-token", result);

        verify(jwtService).extractEmail(refreshToken);
        verify(userRepository).findByEmail(email);
        verify(jwtService).generateAccessToken(user);
    }
}