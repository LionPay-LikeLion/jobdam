package com.jobdam.user.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OAuthRegisterRequestDto {
    private String providerId;          // e.g., Google sub
    private String providerType;        // e.g., GOOGLE, KAKAO, APPLE
    private String email;
    private Boolean emailVerified;
    private String nickname;
    private String profileImageUrl;
}
