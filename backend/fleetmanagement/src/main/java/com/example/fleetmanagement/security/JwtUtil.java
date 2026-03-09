package com.example.fleetmanagement.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

@Component
public class JwtUtil {

    private static final String SECRET =
            "fleetmanagementsecretkeyfleetmanagementsecretkey";

    private static final long EXPIRATION_TIME = 1000 * 60 * 60; // 1 hour

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET.getBytes());
    }

    // ===============================
    // GENERATE TOKEN
    // ===============================
    public String generateToken(String email, String role) {

        return Jwts.builder()
                .setSubject(email)
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // ===============================
    // EXTRACT EMAIL
    // ===============================
    public String extractEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // ===============================
    // EXTRACT ROLE
    // ===============================
    public String extractRole(String token) {
        return extractAllClaims(token).get("role", String.class);
    }

    // ===============================
    // GENERIC CLAIM EXTRACTION
    // ===============================
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // ===============================
    // GET ALL CLAIMS
    // ===============================
    private Claims extractAllClaims(String token) {

        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // ===============================
    // CHECK TOKEN EXPIRATION
    // ===============================
    private boolean isTokenExpired(String token) {
        return extractClaim(token, Claims::getExpiration)
                .before(new Date());
    }

    // ===============================
    // VALIDATE TOKEN
    // ===============================
    public boolean validateToken(String token) {

        try {

            return !isTokenExpired(token);

        } catch (JwtException | IllegalArgumentException e) {

            return false;

        }
    }
}