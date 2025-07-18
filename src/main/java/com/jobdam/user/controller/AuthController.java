package com.jobdam.user.controller;

import com.jobdam.user.dto.LoginRequestDto;
import com.jobdam.user.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequestDto request) {
        System.out.println(">>>>> [Controller] login 요청 들어옴: " + request.getEmail());
        String token = authService.login(request);
        System.out.println(">>>>> [Controller] 생성된 토큰: " + token);
        return ResponseEntity.ok(token);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        System.out.println(">>> [AuthController] 로그아웃 요청 처리 완료");
        // react에서 accessToken 삭제
        return ResponseEntity.ok("로그아웃 완료");
    }
}
