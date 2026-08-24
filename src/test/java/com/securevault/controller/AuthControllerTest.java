package com.securevault.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.securevault.dto.AuthResponse;
import com.securevault.dto.LoginRequest;
import com.securevault.dto.RegisterRequest;
import com.securevault.service.AuthService;
import com.securevault.service.RateLimitingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    private MockMvc mockMvc;

    @Mock
    private AuthService authService;

    @Mock
    private RateLimitingService rateLimitingService;

    @InjectMocks
    private AuthController authController;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(authController)
                .build();

        objectMapper = new ObjectMapper();
    }

    @Test
    void register_shouldReturnSuccessMessage() throws Exception {

        RegisterRequest request = new RegisterRequest();
        request.setName("Swastika");
        request.setEmail("swastika@gmail.com");
        request.setPassword("password123");

        when(authService.register(any(RegisterRequest.class)))
                .thenReturn("User registered successfully");

        mockMvc.perform(
                        post("/api/auth/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isOk())
                .andExpect(content().string("User registered successfully"));
    }

    @Test
    void login_shouldReturnAuthResponse() throws Exception {

        LoginRequest request = new LoginRequest();
        request.setEmail("swastika@gmail.com");
        request.setPassword("password123");

        AuthResponse response =
                new AuthResponse(
                        "access-token",
                        "refresh-token"
                );

        when(rateLimitingService.isAllowed("swastika@gmail.com"))
                .thenReturn(true);

        when(authService.login(any(LoginRequest.class)))
                .thenReturn(response);

        mockMvc.perform(
                        post("/api/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value("access-token"))
                .andExpect(jsonPath("$.refreshToken").value("refresh-token"));
    }

    @Test
    void login_shouldReturn429WhenRateLimitExceeded() throws Exception {

        LoginRequest request = new LoginRequest();
        request.setEmail("swastika@gmail.com");
        request.setPassword("password123");

        when(rateLimitingService.isAllowed("swastika@gmail.com"))
                .thenReturn(false);

        mockMvc.perform(
                        post("/api/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isTooManyRequests());
    }
}