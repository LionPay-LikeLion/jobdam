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
        return savedCode != null && savedCode.equals(code);
    }

    private String generateCode() {
        Random random = new Random();
        int code = 100000 + random.nextInt(900000); // 100000 ~ 999999
        return String.valueOf(code);
    }
}
