package com.jobdam.user.controller;

import com.jobdam.user.dto.*;
import com.jobdam.user.service.AuthService;
import com.jobdam.user.service.MailService;
import com.jobdam.user.service.UserService;
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
    private final UserService userService;

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

    // === 비밀번호 재설정 엔드포인트 ===
    
    @PostMapping("/password-reset/send-code")
    public ResponseEntity<String> sendPasswordResetCode(@RequestBody @Valid PasswordResetRequestDto dto) {
        // 이메일이 등록되어 있는지 확인
        if (!userService.emailExists(dto.getEmail())) {
            return ResponseEntity.badRequest().body("등록되지 않은 이메일 주소입니다.");
        }
        
        memberService.sendPasswordResetCode(dto.getEmail());
        return ResponseEntity.ok("비밀번호 재설정 인증코드가 이메일로 발송되었습니다.");
    }

    @PostMapping("/password-reset/verify-code")
    public ResponseEntity<Boolean> verifyPasswordResetCode(@RequestBody @Valid PasswordResetVerifyDto dto) {
        boolean result = memberService.checkPasswordResetCode(dto.getEmail(), dto.getCode());
        return ResponseEntity.ok(result);
    }

    @PostMapping("/password-reset/set-new-password")
    public ResponseEntity<String> setNewPassword(@RequestBody @Valid NewPasswordDto dto) {
        // 인증코드 재확인
        boolean isValidCode = memberService.checkPasswordResetCode(dto.getEmail(), dto.getCode());
        if (!isValidCode) {
            return ResponseEntity.badRequest().body("인증코드가 유효하지 않습니다.");
        }
        
        // 비밀번호 재설정
        userService.resetPassword(dto.getEmail(), dto.getNewPassword());
        
        // 사용된 인증코드 삭제
        memberService.removePasswordResetCode(dto.getEmail());
        
        return ResponseEntity.ok("비밀번호가 성공적으로 재설정되었습니다.");
    }
}
