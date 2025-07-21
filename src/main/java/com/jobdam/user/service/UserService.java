package com.jobdam.user.service;

import com.jobdam.code.entity.MemberTypeCode;
import com.jobdam.code.entity.RoleCode;
import com.jobdam.code.entity.SubscriptionLevelCode;
import com.jobdam.code.repository.MemberTypeCodeRepository;
import com.jobdam.code.repository.RoleCodeRepository;
import com.jobdam.code.repository.SubscriptionLevelCodeRepository;
import com.jobdam.user.dto.UserProfileDto;
import com.jobdam.user.entity.User;
import com.jobdam.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.jobdam.user.dto.ChangePasswordRequestDto;
import com.jobdam.user.dto.ChangePasswordRequestDto;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final SubscriptionLevelCodeRepository subscriptionLevelCodeRepository;
    private final RoleCodeRepository roleCodeRepository;
    private final MemberTypeCodeRepository memberTypeCodeRepository;  // MemberTypeCodeRepository 추가

    public UserProfileDto getUserProfile(Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유저가 존재하지 않습니다."));

        // 구독 레벨과 역할 정보 가져오기
        String subscriptionLevel = subscriptionLevelCodeRepository.findById(user.getSubscriptionLevelCodeId())
                .map(SubscriptionLevelCode::getCode)
                .orElse("BASIC");

        String role = roleCodeRepository.findById(user.getRoleCodeId())
                .map(RoleCode::getCode)
                .orElse("USER");

        String memberTypeCode = memberTypeCodeRepository.findById(user.getMemberTypeCodeId())
                .map(MemberTypeCode::getCode)  // 회원 타입 코드 반환
                .orElse("GENERAL");  // 기본값 설정

        return new UserProfileDto(
                user.getEmail(),
                user.getNickname(),
                user.getPoint(),
                subscriptionLevel,
                role,
                user.getPhone(),
                user.getProfileImageUrl(),
                memberTypeCode,  // 회원 타입 코드 포함
                user.getCreatedAt()  // 회원 가입일 포함
        );
    }

    public void updateProfileImage(Integer userId, MultipartFile image) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("유저를 찾을 수 없습니다."));

        String fileUrl = null;
        if (image != null && !image.isEmpty()) {
            String rootPath = System.getProperty("user.dir");
            String uploadDir = rootPath + File.separator + "uploads" + File.separator + "user" + File.separator + userId + File.separator;
            File folder = new File(uploadDir);
            if (!folder.exists()) folder.mkdirs();
            String fileName = UUID.randomUUID() + "_" + image.getOriginalFilename();
            File dest = new File(uploadDir + fileName);

            try {
                image.transferTo(dest);
                fileUrl = "/uploads/user/" + userId + "/" + fileName;
            } catch (IOException | IllegalStateException e) {
                e.printStackTrace();
                throw new RuntimeException("파일 업로드 중 오류가 발생했습니다.", e);
            }
        }

        user.setProfileImageUrl(fileUrl);
        userRepository.save(user);
    }

    public void withdrawUser(Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유저가 존재하지 않습니다."));

        if (Boolean.FALSE.equals(user.getIsActive())) {
            throw new IllegalStateException("이미 탈퇴한 회원입니다.");
        }

        user.deactivate(); // 소프트 삭제
        userRepository.save(user);
    }

    private final PasswordEncoder passwordEncoder;
    public void changePassword(Integer userId, ChangePasswordRequestDto requestDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유저가 존재하지 않습니다."));

        // 현재 비밀번호 확인
        if (!passwordEncoder.matches(requestDto.getCurrentPassword(), user.getPassword())) {
            throw new IllegalArgumentException("현재 비밀번호가 일치하지 않습니다.");
        }

        // 새 비밀번호로 변경
        user.setPassword(passwordEncoder.encode(requestDto.getNewPassword()));
        userRepository.save(user);
    }



}
