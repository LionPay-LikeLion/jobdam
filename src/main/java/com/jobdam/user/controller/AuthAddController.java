package com.jobdam.user.controller;

import com.jobdam.user.dto.RegisterRequestDto;
import com.jobdam.user.dto.VerificationCheckRequestDto;
import com.jobdam.user.dto.VerificationRequestDto;
import com.jobdam.user.service.AuthService;
import com.jobdam.user.service.MailService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthAddController {

    private final AuthService authService;
    private final MailService memberService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody @Valid RegisterRequestDto request) {
        authService.register(request);
        return ResponseEntity.ok("회원가입 성공");
    }

    @GetMapping("/check-id")
    public ResponseEntity<Boolean> checkNickname(@RequestParam String nickname) {
        return ResponseEntity.ok(authService.checkNicknameExists(nickname));
    }

    @GetMapping("/check-email")
    public ResponseEntity<Boolean> checkEmail(@RequestParam String email) {
        return ResponseEntity.ok(authService.checkEmailExists(email));
    }

    @PostMapping("/send-verification")
    public ResponseEntity<String> sendVerification(@RequestBody @Valid VerificationRequestDto dto) {
        memberService.sendVerificationCode(dto.getEmail());
        return ResponseEntity.ok("인증코드 전송 완료");
    }

    @PostMapping("/check-verification")
    public ResponseEntity<Boolean> checkVerification(@RequestBody @Valid VerificationCheckRequestDto dto) {
        boolean result = memberService.checkVerificationCode(dto.getEmail(), dto.getCode());
        return ResponseEntity.ok(result);
    }
}
