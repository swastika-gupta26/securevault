package com.securevault.service;

import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.util.Date;

@Service
public class JwtService {
    private final SecretKey key;
    private final long accessTokenExpiration = 15 * 60 * 1000;

    public JwtService(@Value("${app.jwt.secret}") String base64Secret) {
        byte[] decodedKey = Base64.getDecoder().decode(base64Secret);
        this.key = new SecretKeySpec(decodedKey, "HmacSHA256");
    }

    public String generateAccessToken(String email) {
        return Jwts.builder()
                .subject(email)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis()
                        + accessTokenExpiration))
                .signWith(key)
                .compact();
    }

    public String generateRefreshToken(String email) {
        return Jwts.builder()
                .subject(email)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis()
                        + 7L * 24 * 60 * 60 * 1000))
                .signWith(key)
                .compact();
    }

    public String extractEmail(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }
}