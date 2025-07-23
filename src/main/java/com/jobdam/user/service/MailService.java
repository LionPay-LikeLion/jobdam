package com.jobdam.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender mailSender;

    // 메모리 Map 저장소 (테스트용)
    private final Map<String, String> verificationStorage = new ConcurrentHashMap<>();
    private final Map<String, String> passwordResetStorage = new ConcurrentHashMap<>();

    public void sendVerificationCode(String email) {
        String code = generateCode();

        verificationStorage.put(email, code);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("[Jobdam] 이메일 인증코드");
        message.setText("인증코드: " + code);
        mailSender.send(message);

        System.out.println("[DEBUG] 이메일: " + email + ", 인증코드: " + code);
    }

    public boolean checkVerificationCode(String email, String code) {
        String savedCode = verificationStorage.get(email);
        boolean isValid = savedCode != null && savedCode.equals(code);
        
        // 인증 완료 후 코드 삭제 (재사용 방지)
        if (isValid) {
            verificationStorage.remove(email);
            System.out.println("[DEBUG] 이메일 인증 확인 완료: " + email);
        }
        
        return isValid;
    }

    public void sendPasswordResetCode(String email) {
        String code = generateCode();

        passwordResetStorage.put(email, code);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("[Jobdam] 비밀번호 재설정 인증코드");
        message.setText("비밀번호 재설정을 위한 인증코드: " + code + "\n\n이 코드를 사용하여 새 비밀번호를 설정하세요.");
        mailSender.send(message);

        System.out.println("[DEBUG] 비밀번호 재설정 이메일: " + email + ", 인증코드: " + code);
    }

    public boolean checkPasswordResetCode(String email, String code) {
        String savedCode = passwordResetStorage.get(email);
        boolean isValid = savedCode != null && savedCode.equals(code);
        
        // 인증 완료 후 코드 삭제는 비밀번호 재설정 완료 후에
        if (isValid) {
            System.out.println("[DEBUG] 비밀번호 재설정 인증 확인 완료: " + email);
        }
        
        return isValid;
    }

    public void removePasswordResetCode(String email) {
        passwordResetStorage.remove(email);
        System.out.println("[DEBUG] 비밀번호 재설정 코드 삭제: " + email);
    }

    private String generateCode() {
        Random random = new Random();
        int code = 100000 + random.nextInt(900000); // 100000 ~ 999999
        return String.valueOf(code);
    }
}
