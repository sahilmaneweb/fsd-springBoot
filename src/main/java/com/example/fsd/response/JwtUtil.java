package com.example.fsd.response;

import io.jsonwebtoken.*;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class JwtUtil {

    private static final String SECRET_KEY = "Sahil";

    // Custom Exception
    public static class JwtException extends RuntimeException {
        public JwtException(String message) {
            super(message);
        }
    }

    // Generate token with Bearer prefix
    public String generateToken(String username, Map<String, Object> extraClaims) {
        String token = Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // 10 hours
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
        return "Bearer " + token;
    }

    // Validate token format and expiration
    public void validateToken(String token) {
        if (token.startsWith("Bearer ")) token = token.substring(7);
        try {
            getClaims(token);
        } catch (ExpiredJwtException e) {
            throw new JwtException("Token expired");
        } catch (JwtException | IllegalArgumentException e) {
            throw new JwtException("Invalid token: " + e.getMessage());
        }
    }

    // Extract claims
    public Claims getClaims(String token) {
        if (token.startsWith("Bearer ")) token = token.substring(7);
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();
    }

    // Extract username
    public String extractUsername(String token) {
        return getClaims(token).getSubject();
    }

    // Extract custom data
    public Object extractData(String token, String key) {
        return getClaims(token).get(key);
    }

    // Validate role from allowed roles
    public Claims validateRole(String token, List<String> allowedRoles) {
        validateToken(token);
        Claims claims = getClaims(token);
        Object roleObj = claims.get("role");

        if (roleObj instanceof String role) {
            if (allowedRoles.contains(role)) return claims;
            else throw new JwtException("Access denied: Role not allowed");
        } else {
            throw new JwtException("Missing or invalid role in token");
        }
    }
}

