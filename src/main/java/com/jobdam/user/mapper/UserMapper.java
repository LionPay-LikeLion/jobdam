package com.jobdam.user.mapper;

import com.jobdam.code.repository.MemberTypeCodeRepository;
import com.jobdam.code.repository.RoleCodeRepository;
import com.jobdam.code.repository.SubscriptionLevelCodeRepository;
import com.jobdam.user.dto.RegisterRequestDto;
import com.jobdam.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class UserMapper {

    private final SubscriptionLevelCodeRepository subscriptionLevelCodeRepository;
    private final RoleCodeRepository roleCodeRepository;
    private final MemberTypeCodeRepository memberTypeCodeRepository;

    public User toEntity(RegisterRequestDto request, String encodedPassword) {

        var subscriptionLevelCode = subscriptionLevelCodeRepository.findByCode("BASIC")
                .orElseThrow(() -> new IllegalArgumentException("구독 등급 코드가 없습니다."));

        var roleCode = roleCodeRepository.findByCode("USER")
                .orElseThrow(() -> new IllegalArgumentException("역할 코드가 없습니다."));

        var memberTypeCode = memberTypeCodeRepository.findByCode("GENERAL")
                .orElseThrow(() -> new IllegalArgumentException("회원 타입 코드가 없습니다."));

        return User.builder()
                .email(request.getEmail())
                .password(encodedPassword)
                .nickname(request.getNickname())
                .phone(request.getPhone())
                .profileImageUrl(request.getProfileImageUrl())
                .subscriptionLevelCodeId(subscriptionLevelCode.getSubscriptionLevelCodeId())
                .roleCodeId(roleCode.getRoleCodeId())
                .memberTypeCodeId(memberTypeCode.getMemberTypeCodeId())
                .point(0)
                .build();
    }
}

