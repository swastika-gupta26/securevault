package com.securevault.controller;

import com.securevault.dto.AuthResponse;
import com.securevault.dto.LoginRequest;
import com.securevault.dto.RegisterRequest;
import com.securevault.service.AuthService;
import com.securevault.service.RateLimitingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
@Tag(
        name = "Authentication",
        description = "APIs for user registration, login and token refresh"
)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @Autowired
    private RateLimitingService rateLimitingService;
    @Operation(
            summary = "Register a new user",
            description = "Creates a new user account"
    )
    @PostMapping("/register")
    public String register(@Valid @RequestBody RegisterRequest registerRequest){
        return authService.register(registerRequest);
    }
    @Operation(
            summary = "User login",
            description = "Authenticates the user and returns access and refresh tokens"
    )
    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody LoginRequest loginRequest){
        String key = loginRequest.getEmail();
        if(!rateLimitingService.isAllowed(key)){
            throw new ResponseStatusException(
                    HttpStatus.TOO_MANY_REQUESTS,
                    "Too many login attempts. Try again later."
            );
        }
        return authService.login(loginRequest);
    }
    @Operation(
            summary = "Refresh access token",
            description = "Generates a new access token using a valid refresh token"
    )
    @PostMapping("/refresh")
    public String refresh(@RequestParam String refreshToken){

        return authService.refreshAccessToken(refreshToken);
    }
}
