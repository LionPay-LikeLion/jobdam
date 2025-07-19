package com.jobdam.user.service;

import com.jobdam.code.entity.MemberTypeCode;
import com.jobdam.code.entity.RoleCode;
import com.jobdam.code.entity.SubscriptionLevelCode;
import com.jobdam.code.repository.MemberTypeCodeRepository;
import com.jobdam.code.repository.RoleCodeRepository;
import com.jobdam.code.repository.SubscriptionLevelCodeRepository;
import com.jobdam.user.dto.OAuthRegisterRequestDto;
import com.jobdam.user.dto.UserProfileDto;
import com.jobdam.user.entity.User;
import com.jobdam.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

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

    public User registerOAuthUser(OAuthRegisterRequestDto dto) {
        // does user already exist (providerId or by email as fallback)
        Optional<User> existing = userRepository.findByProviderId(dto.getProviderId());
        if (existing.isPresent()) {
            return existing.get();  // or throw if you want to enforce single registration
        }

        // build new user
        User newUser = User.builder()
                .providerId(dto.getProviderId())
                .providerType(dto.getProviderType())
                .email(dto.getEmail())
                .emailVerified(dto.getEmailVerified())
                .nickname(dto.getNickname())
                .profileImageUrl(dto.getProfileImageUrl())
                .subscriptionLevelCodeId(1)     // default
                .roleCodeId(1)                  // default
                .memberTypeCodeId(1)            // default
                .point(0)
                .build();

        // save and return
        return userRepository.save(newUser);
    }

    public User findOrRegisterOAuthUser(OAuthRegisterRequestDto dto) {
        if (!dto.getEmailVerified()) {
            throw new IllegalStateException("Google has not verified your email. Please verify it before logging in.");
        }
        // Check if user exists by provider ID
        Optional<User> existingUser = userRepository.findByProviderId(dto.getProviderId());
        return existingUser.orElseGet(() -> registerOAuthUser(dto));
    }
}
