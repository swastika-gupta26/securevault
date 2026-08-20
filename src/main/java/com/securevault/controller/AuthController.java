package com.securevault.controller;

import com.securevault.dto.AuthResponse;
import com.securevault.dto.LoginRequest;
import com.securevault.dto.RegisterRequest;
import com.securevault.service.AuthService;
import com.securevault.service.RateLimitingService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @Autowired
    private RateLimitingService rateLimitingService;

    @PostMapping("/register")
    public String register(@Valid @RequestBody RegisterRequest registerRequest){
        return authService.register(registerRequest);
    }

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

    @PostMapping("/refresh")
    public String refresh(@RequestParam String refreshToken){

        return authService.refreshAccessToken(refreshToken);
    }
}
