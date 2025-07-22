package com.jobdam.common.util;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
public class KakaoApiClient {

    private final RestTemplate restTemplate;
    private final String kakaoUserInfoUrl = "https://kapi.kakao.com/v2/user/me";

    public KakaoApiClient() {
        this.restTemplate = new RestTemplate();
    }

    public KakaoUserInfo getUserInfo(String accessToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + accessToken);
            headers.set("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");

            HttpEntity<String> entity = new HttpEntity<>(headers);
            
            ResponseEntity<KakaoUserInfo> response = restTemplate.exchange(
                kakaoUserInfoUrl,
                HttpMethod.GET,
                entity,
                KakaoUserInfo.class
            );

            return response.getBody();
        } catch (Exception e) {
            log.error("Failed to get Kakao user info", e);
            return null;
        }
    }

    @Data
    public static class KakaoUserInfo {
        private Long id;
        
        @JsonProperty("connected_at")
        private String connectedAt;

        @JsonProperty("kakao_account")
        private KakaoAccount kakaoAccount;

        @Data
        public static class KakaoAccount {
            @JsonProperty("profile_nickname_needs_agreement")
            private Boolean profileNicknameNeedsAgreement;

            @JsonProperty("profile_image_needs_agreement")
            private Boolean profileImageNeedsAgreement;

            @JsonProperty("profile")
            private Profile profile;

            @JsonProperty("has_email")
            private Boolean hasEmail;

            @JsonProperty("email_needs_agreement")
            private Boolean emailNeedsAgreement;

            @JsonProperty("is_email_valid")
            private Boolean isEmailValid;

            @JsonProperty("is_email_verified")
            private Boolean isEmailVerified;

            @JsonProperty("email")
            private String email;

            @Data
            public static class Profile {
                @JsonProperty("nickname")
                private String nickname;

                @JsonProperty("thumbnail_image_url")
                private String thumbnailImageUrl;

                @JsonProperty("profile_image_url")
                private String profileImageUrl;

                @JsonProperty("is_default_image")
                private Boolean isDefaultImage;

                @JsonProperty("is_default_nickname")
                private Boolean isDefaultNickname;
            }
        }
    }
}