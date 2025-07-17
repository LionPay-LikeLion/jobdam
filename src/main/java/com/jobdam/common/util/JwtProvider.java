package com.jobdam.common.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtProvider {

    @Value("${jwt.secret}")
    private String secretKey;

    private final long accessTokenValidTime = 1000L * 60 * 60;

    private Key getSigningKey() {
        byte[] keyBytes = Base64.getDecoder().decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String createToken(String email, int roleCodeId) {
        System.out.println(">>>>> [JwtProvider] JWT 생성 시작 - 이메일: " + email + ", roleId: " + roleCodeId);

        Claims claims = Jwts.claims().setSubject(email);
        claims.put("role", roleCodeId);

        Date now = new Date();
        Date validity = new Date(now.getTime() + accessTokenValidTime);

        String token = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();

        System.out.println(">>>>> [JwtProvider] JWT 생성 완료: " + token);

        return token;
    }

    public Claims parseClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}

