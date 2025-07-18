package com.jobdam.user.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/test")
public class JwtTestController {

    @GetMapping("/userinfo")
    public ResponseEntity<?> getUserInfo(Authentication authentication) {
        // SecurityContext에 인증된 사용자가 있으면 여기로 들어옴
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }

        String userId = authentication.getName(); // 보통 JWT의 subject
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        Map<String, Object> result = new HashMap<>();
        result.put("userId", userId);
        result.put("roles", authorities);

        return ResponseEntity.ok(result);
    }
}
