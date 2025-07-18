
package com.jobdam;

import com.jobdam.common.util.JwtProvider;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Base64;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

public class JwtProviderTest {

    private JwtProvider jwtProvider;
    private String rawSecretKey = "MHByb2plY3Qwam9iZGFtMGZvcmV2ZXIwbGlrZWxpb24=";
    private String encodedSecretKey;

    @BeforeEach
    void setUp() {
        jwtProvider = new JwtProvider();
        encodedSecretKey = Base64.getEncoder().encodeToString(rawSecretKey.getBytes(StandardCharsets.UTF_8));
        ReflectionTestUtils.setField(jwtProvider, "secretKey", encodedSecretKey);
    }

    private String createExpiredToken() {
        Key key = Keys.hmacShaKeyFor(Base64.getDecoder().decode(encodedSecretKey));
        return io.jsonwebtoken.Jwts.builder()
                .setSubject("1")
                .claim("email", "expired@example.com")
                .claim("role", "USER")
                .setIssuedAt(new Date(System.currentTimeMillis() - 100000))
                .setExpiration(new Date(System.currentTimeMillis() - 1000)) // 이미 만료된 시간
                .signWith(key)
                .compact();
    }


    @Test
    void createToken_shouldReturnValidToken() {
        String token = jwtProvider.createToken(1,"user@example.com", "USER");
        assertNotNull(token);
    }

    @Test
    void parseClaims_shouldReturnClaimsWithCorrectSubjectAndRole() {
        String token = jwtProvider.createToken(1, "user@example.com", "USER");
        Claims claims = jwtProvider.parseClaims(token);

        assertEquals("1", claims.getSubject()); // sub가 userId로 변경됨
        assertEquals("user@example.com", claims.get("email", String.class)); // email은 claim으로
        assertEquals("USER", claims.get("role", String.class)); // role claim도 그대로 확인
    }


    @Test
    void parseClaims_shouldThrowExceptionForExpiredToken() {
        String expiredToken = createExpiredToken();
        assertThrows(ExpiredJwtException.class, () -> jwtProvider.parseClaims(expiredToken));
    }
}
