package com.smartshop.backend.security;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

    // Secret key (change this in production)
    private static final String SECRET =
            "mySecretKeyForSmartShopManagerApplication123456789";

    private final Key key = Keys.hmacShaKeyFor(SECRET.getBytes());

    // Generate JWT Token
    public String generateToken(String username) {

        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 86400000))
                .signWith(key)
                .compact();
    }
// Extract username from JWT
public String extractUsername(String token) {
    return extractClaim(token, Claims::getSubject);
}

// Extract expiration date
public Date extractExpiration(String token) {
    return extractClaim(token, Claims::getExpiration);
}

// Generic claim extractor
public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {

    Claims claims = Jwts.parser()
            .verifyWith((javax.crypto.SecretKey) key)
            .build()
            .parseSignedClaims(token)
            .getPayload();

    return claimsResolver.apply(claims);
}

// Check if token expired
private boolean isTokenExpired(String token) {
    return extractExpiration(token).before(new Date());
}

// Validate token
public boolean validateToken(String token, String username) {

    String extractedUsername = extractUsername(token);

    return extractedUsername.equals(username)
            && !isTokenExpired(token);
}
}