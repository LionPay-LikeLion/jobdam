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
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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
    @Transactional
    public void deactivateUser(Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("해당 회원이 없습니다."));
        user.setIsActive(false);
        userRepository.save(user);
    }

}
