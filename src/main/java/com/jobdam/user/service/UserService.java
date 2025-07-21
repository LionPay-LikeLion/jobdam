package com.jobdam.user.service;

import com.jobdam.common.util.JwtProvider;
import com.jobdam.user.dto.LoginResponseDto;
import com.jobdam.user.dto.OAuthRegisterRequestDto;

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
import org.springframework.security.crypto.password.PasswordEncoder;
import com.jobdam.user.dto.ChangePasswordRequestDto;
import com.jobdam.user.dto.ChangePasswordRequestDto;

import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final SubscriptionLevelCodeRepository subscriptionLevelCodeRepository;
    private final RoleCodeRepository roleCodeRepository;
    private final MemberTypeCodeRepository memberTypeCodeRepository;
    private final JwtProvider jwtProvider;

    public UserProfileDto getUserProfile(Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유저가 존재하지 않습니다."));

        String subscriptionLevel = subscriptionLevelCodeRepository.findById(user.getSubscriptionLevelCodeId())
                .map(SubscriptionLevelCode::getCode)
                .orElse("BASIC");

        String role = roleCodeRepository.findById(user.getRoleCodeId())
                .map(RoleCode::getCode)
                .orElse("USER");

        String memberTypeCode = memberTypeCodeRepository.findById(user.getMemberTypeCodeId())
                .map(MemberTypeCode::getCode)
                .orElse("GENERAL");

        return UserProfileDto.builder()
                .userId(user.getUserId())
                .email(user.getEmail())
                .nickname(user.getNickname())
                .remainingPoints(user.getPoint())
                .subscriptionLevel(subscriptionLevel)
                .role(role)
                .phone(user.getPhone())
                .profileImageUrl(user.getProfileImageUrl())
                .memberTypeCode(memberTypeCode)
                .createdAt(user.getCreatedAt())
                .build();
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

    @Transactional
    public void deactivateUser(Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("해당 회원이 없습니다."));
        user.setIsActive(false);
        userRepository.save(user);
    }

    public User findOrRegisterOAuthUser(OAuthRegisterRequestDto dto) {
        System.out.println("DEBUG: Checking for existing Google user with email: " + dto.getEmail());

        // Step 1: Get GOOGLE memberTypeId
        Integer googleMemberTypeId = memberTypeCodeRepository.findByCode("GOOGLE")
                .map(MemberTypeCode::getMemberTypeCodeId)
                .orElse(1);

        // Step 2: Try finding existing Google user (by email + GOOGLE)
        Optional<User> existingGoogleUser = userRepository.findByEmailAndMemberTypeCodeId(dto.getEmail(), googleMemberTypeId);
        if (existingGoogleUser.isPresent()) {
            System.out.println("DEBUG: Found existing Google user with email: " + dto.getEmail());
            return existingGoogleUser.get();
        }

        // Step 3: Check for email conflict with non-Google user
        Optional<User> conflictingUser = userRepository.findByEmail(dto.getEmail());
        if (conflictingUser.isPresent()) {
            System.out.println("ERROR: Email conflict - user exists but not as Google login: " + dto.getEmail());
            throw new IllegalStateException("This email is already registered using another method.");
        }

        // Step 4: Create new Google user
        System.out.println("DEBUG: No existing user found. Proceeding to register new Google user.");

        Integer roleCodeId = roleCodeRepository.findByCode("USER")
                .map(RoleCode::getRoleCodeId)
                .orElse(1);
        Integer subscriptionId = subscriptionLevelCodeRepository.findByCode("BASIC")
                .map(SubscriptionLevelCode::getSubscriptionLevelCodeId)
                .orElse(1);

        System.out.println("DEBUG: roleCodeId=" + roleCodeId +
                ", subscriptionId=" + subscriptionId +
                ", memberTypeId=" + googleMemberTypeId);

        User newUser = User.builder()
                .email(dto.getEmail())
                .nickname(dto.getNickname())
                .roleCodeId(roleCodeId)
                .subscriptionLevelCodeId(subscriptionId)
                .memberTypeCodeId(googleMemberTypeId)
                .profileImageUrl(dto.getProfileImageUrl())
                .emailVerified(dto.getEmailVerified())
                .point(0)
                .build();

        User savedUser = userRepository.save(newUser);
        System.out.println("DEBUG: New Google user saved with userId: " + savedUser.getUserId());
        return savedUser;
    }

    public LoginResponseDto buildLoginResponse(User user) {
        String accessToken = jwtProvider.createToken(user.getUserId(), user.getEmail(), user.getRoleCode().getCode());
        String refreshToken = jwtProvider.createRefreshToken(user.getUserId(), user.getEmail(), user.getRoleCode().getCode());

        UserProfileDto profile = UserProfileDto.builder()
                .email(user.getEmail())
                .nickname(user.getNickname())
                .remainingPoints(user.getPoint())
                .subscriptionLevel(user.getSubscriptionLevelCode().getName())
                .role(user.getRoleCode().getCode())
                .phone(user.getPhone())
                .profileImageUrl(user.getProfileImageUrl())
                .memberTypeCode(user.getMemberTypeCode().getName())
                .createdAt(user.getCreatedAt())
                .build();

        return new LoginResponseDto(accessToken, refreshToken, profile);
    }
}
