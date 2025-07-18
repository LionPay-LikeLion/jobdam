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

    public String createToken(Integer userId, String email, String roleCode) {
        System.out.println(">>>>> [JwtProvider] JWT 생성 시작 - userId: " + userId + ", 이메일: " + email + ", role: " + roleCode);

        Date now = new Date(); // 현재 시간
        Date validity = new Date(now.getTime() + accessTokenValidTime); // 만료 시간

        Claims claims = Jwts.claims().setSubject(userId.toString()); // sub를 userId로 설정
        claims.put("email", email);
        claims.put("role", roleCode);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)         // iat
                .setExpiration(validity)  // exp
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }


    public Claims parseClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}

