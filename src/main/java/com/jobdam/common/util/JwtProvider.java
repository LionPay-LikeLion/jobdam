package com.jobdam.common.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Slf4j
@Component
public class JwtProvider {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.access-token-validity:300000}")
    private long accessTokenValidTime;

    @Value("${jwt.refresh-token-validity:1209600000}")
    private long refreshTokenValidTime;

    private Key key;

    @PostConstruct
    protected void init() {
        byte[] decodedKey = Base64.getEncoder().encode(secretKey.getBytes());
        this.key = Keys.hmacShaKeyFor(decodedKey);
    }

    public String createToken(Integer userId, String email, String roleCode) {
        return createTokenWithCustomValidity(userId, email, roleCode, accessTokenValidTime);
    }

    public String createRefreshToken(Integer userId, String email, String roleCode) {
        return createTokenWithCustomValidity(userId, email, roleCode, refreshTokenValidTime);
    }

    public String createAccessToken(com.jobdam.user.entity.User user) {
        return createToken(user.getUserId(), user.getEmail(), user.getRoleCode().getCode());
    }

    public String createRefreshToken(com.jobdam.user.entity.User user) {
        return createRefreshToken(user.getUserId(), user.getEmail(), user.getRoleCode().getCode());
    }

    private String createTokenWithCustomValidity(Integer userId, String email, String roleCode, long validityMillis) {

        Date now = new Date();
        Date expiry = new Date(now.getTime() + validityMillis);

        Claims claims = Jwts.claims().setSubject(userId.toString());
        claims.put("email", email);
        claims.put("role", roleCode);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

    }

    public boolean validateToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (JwtException | IllegalArgumentException e) {
            log.warn("❌ Invalid or expired JWT token: {}", e.getMessage());
            return false;
        }
    }

    public Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String reissueAccessToken(String refreshToken) {
        if (!validateToken(refreshToken)) {
            throw new IllegalArgumentException("Refresh token is invalid or expired");
        }

        Claims claims = getClaims(refreshToken);
        Integer userId = Integer.parseInt(claims.getSubject());
        String email = claims.get("email", String.class);
        String role = claims.get("role", String.class);

        return createToken(userId, email, role);
    }
}