package com.jobdam.user.controller;

import com.jobdam.common.util.JwtProvider;
import com.jobdam.user.dto.LoginRequestDto;
import com.jobdam.user.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final JwtProvider jwtProvider;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequestDto request) {
        log.info("👉 [AuthController] login 요청: {}", request.getEmail());
        String token = authService.login(request);
        log.info("👉 [Controller] 생성된 토큰: {}", token);
        return ResponseEntity.ok(token);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        // TODO: invalidate refresh token (e.g., in Redis or DB)
        log.info("👋 로그아웃 처리 완료 (프론트에서 토큰 삭제)");
        return ResponseEntity.ok("로그아웃 완료");
    }

    @PostMapping("/reissue")
    public ResponseEntity<Map<String, String>> reissue(@RequestBody Map<String, String> request) {
        String refreshToken = request.get("refreshToken");

        if (refreshToken == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Missing refresh token"));
        }

        try {
            String newAccessToken = jwtProvider.reissueAccessToken(refreshToken);
            return ResponseEntity.ok(Map.of("accessToken", newAccessToken));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(401).body(Map.of("error", "Invalid or expired refresh token"));
        }
    }
}
