package com.smile.review.security.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Component
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration}")
    private long jwtExpiration;

    @Value("${jwt.refresh-expiration}")
    private long jwtRefreshExpiration;

    private SecretKey secretKey;

    @PostConstruct
    public void init() {
        byte[] keyBytes = Base64.getDecoder().decode(jwtSecret.getBytes());
        secretKey = Keys.hmacShaKeyFor(keyBytes);
    }

    // access token 생성 메소드
    public String createToken(String userId, String role, Long id) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpiration);
        return Jwts.builder()
                .subject(userId)
                .claim("role", role)
                .claim("id", id)
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(secretKey)
                .compact();
    }

    // refresh token 생성 메소드
    public String createRefreshToken(String userId, String role, Long id) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtRefreshExpiration);
        return Jwts.builder()
                .subject(userId)
                .claim("role", role)
                .claim("id", id)
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(secretKey)
                .compact();
    }

    public long getRefreshExpiration() {
        return jwtRefreshExpiration;
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            throw new BadCredentialsException("Invalid JWT Token", e);
        } catch (ExpiredJwtException e) {
            throw new BadCredentialsException("Expired JWT Token", e);
        } catch (UnsupportedJwtException e) {
            throw new BadCredentialsException("Unsupported JWT Token", e);
        } catch (IllegalArgumentException e) {
            throw new BadCredentialsException("JWT Token claims empty", e);
        }
    }

    public String getUserIdFromJWT(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return claims.getSubject();
    }

    public List<String> getRoles(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();

        Object roleObj = claims.get("role");  // "roles"가 아니라 "role"
        if (roleObj == null) return Collections.emptyList();

        // 단일 role이면 문자열, 여러 개면 배열일 수 있음(지금은 단일 문자열로 발급)
        return Collections.singletonList(roleObj.toString());
    }



    public String resolveToken(HttpServletRequest request) {
        String bearer = request.getHeader("Authorization");
        if (bearer != null && bearer.startsWith("Bearer ")) {
            return bearer.substring(7);
        }
        return null;
    }

}
