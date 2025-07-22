package com.jobdam.user.service;

import com.jobdam.common.service.FileService;
import com.jobdam.common.util.JwtProvider;
import com.jobdam.user.dto.*;

import com.jobdam.code.entity.MemberTypeCode;
import com.jobdam.code.entity.RoleCode;
import com.jobdam.code.entity.SubscriptionLevelCode;
import com.jobdam.code.repository.MemberTypeCodeRepository;
import com.jobdam.code.repository.RoleCodeRepository;
import com.jobdam.code.repository.SubscriptionLevelCodeRepository;
import com.jobdam.user.entity.User;
import com.jobdam.user.repository.UserRepository;
import com.mongodb.client.gridfs.model.GridFSFile;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.jobdam.user.dto.ChangePasswordRequestDto;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final SubscriptionLevelCodeRepository subscriptionLevelCodeRepository;
    private final RoleCodeRepository roleCodeRepository;
    private final MemberTypeCodeRepository memberTypeCodeRepository;
    private final JwtProvider jwtProvider;
    private final FileService fileService;

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
                .roleCodeId(user.getRoleCodeId())
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

        if (image != null && !image.isEmpty()) {
            String fileId = fileService.saveFile(image);
            user.setProfileImageUrl("/api/files/" + fileId);  // <<<<<<<<<< 여기만 수정!
            userRepository.save(user);
        }
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
        System.out.println("DEBUG: Checking for existing OAuth user with email: " + dto.getEmail() + " and provider: " + dto.getProviderType());
        
        // Step 1: Try finding existing OAuth user by provider_id and provider_type
        Optional<User> existingOAuthUser = userRepository.findByProviderIdAndProviderType(dto.getProviderId(), dto.getProviderType());
        if (existingOAuthUser.isPresent()) {
            System.out.println("DEBUG: Found existing OAuth user with provider_id: " + dto.getProviderId());
            return existingOAuthUser.get();
        }

        // Step 2: Check for existing user with same email and link OAuth provider
        Optional<User> existingUser = userRepository.findByEmail(dto.getEmail());
        if (existingUser.isPresent()) {
            User user = existingUser.get();
            System.out.println("DEBUG: Found existing user with email: " + dto.getEmail() + ", linking OAuth provider: " + dto.getProviderType());
            
            // Update existing user with OAuth provider information
            user.setProviderId(dto.getProviderId());
            user.setProviderType(dto.getProviderType());
            user.setProfileImageUrl(dto.getProfileImageUrl());
            user.setEmailVerified(dto.getEmailVerified());
            
            User updatedUser = userRepository.save(user);
            System.out.println("DEBUG: Existing user linked with OAuth provider: " + dto.getProviderType());
            return updatedUser;
        }

        // Step 3: Create new OAuth user
        System.out.println("DEBUG: No existing user found. Proceeding to register new OAuth user with provider: " + dto.getProviderType());

        Integer roleCodeId = roleCodeRepository.findByCode("USER")
                .map(RoleCode::getRoleCodeId)
                .orElse(1);
        Integer subscriptionId = subscriptionLevelCodeRepository.findByCode("BASIC")
                .map(SubscriptionLevelCode::getSubscriptionLevelCodeId)
                .orElse(1);
        Integer memberTypeId = memberTypeCodeRepository.findByCode("GENERAL")
                .map(MemberTypeCode::getMemberTypeCodeId)
                .orElse(1);

        System.out.println("DEBUG: roleCodeId=" + roleCodeId +
                ", subscriptionId=" + subscriptionId +
                ", memberTypeId=" + memberTypeId);

        User newUser = User.builder()
                .email(dto.getEmail())
                .nickname(dto.getNickname())
                .roleCodeId(roleCodeId)
                .subscriptionLevelCodeId(subscriptionId)
                .memberTypeCodeId(memberTypeId)
                .providerId(dto.getProviderId())
                .providerType(dto.getProviderType())
                .profileImageUrl(dto.getProfileImageUrl())
                .emailVerified(dto.getEmailVerified())
                .point(0)
                .isActive(true)
                .build();

        User savedUser = userRepository.save(newUser);
        System.out.println("DEBUG: New OAuth user saved with userId: " + savedUser.getUserId());
        return savedUser;
    }

    public LoginResponseDto buildLoginResponse(User user) {
        // Safely get role code
        String roleCode = roleCodeRepository.findById(user.getRoleCodeId())
                .map(RoleCode::getCode)
                .orElse("USER");
        
        String accessToken = jwtProvider.createToken(user.getUserId(), user.getEmail(), roleCode);
        String refreshToken = jwtProvider.createRefreshToken(user.getUserId(), user.getEmail(), roleCode);

        // Safely get subscription level name
        String subscriptionLevel = subscriptionLevelCodeRepository.findById(user.getSubscriptionLevelCodeId())
                .map(SubscriptionLevelCode::getName)
                .orElse("BASIC");
        
        // Safely get member type name
        String memberTypeName = memberTypeCodeRepository.findById(user.getMemberTypeCodeId())
                .map(MemberTypeCode::getName)
                .orElse("GENERAL");

        UserProfileDto profile = UserProfileDto.builder()
                .userId(user.getUserId())
                .email(user.getEmail())
                .nickname(user.getNickname())
                .remainingPoints(user.getPoint())
                .subscriptionLevel(subscriptionLevel)
                .role(roleCode)
                .phone(user.getPhone())
                .profileImageUrl(user.getProfileImageUrl())
                .memberTypeCode(memberTypeName)
                .createdAt(user.getCreatedAt())
                .build();

        return new LoginResponseDto(accessToken, refreshToken, profile);
    }

    public List<UserSearchResponseDto> searchUsersByNickname(String keyword, Integer excludeUserId) {
        List<User> users = userRepository.findByNicknameContainingIgnoreCaseAndUserIdNot(
                keyword, excludeUserId, PageRequest.of(0, 10)
        );

        return users.stream()
                .map(user -> UserSearchResponseDto.builder()
                        .userId(user.getUserId())
                        .nickname(user.getNickname())
                        .profileImageUrl(user.getProfileImageUrl())
                        .subscriptionLevelCode(user.getSubscriptionLevelCode() != null ? user.getSubscriptionLevelCode().getCode() : null)
                        .memberTypeCode(user.getMemberTypeCode() != null ? user.getMemberTypeCode().getCode() : null)
                        .build())
                .collect(Collectors.toList());
    }

    public GridFsResource loadProfileImage(String fileId) {
        return fileService.loadFile(fileId);
    }

}
