package com.jobdam.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponseDto {
    private Integer userId;
    private String token;
    private String email;
    private String nickname;
    private String profileImageUrl;
    private String role;
    private String subscriptionLevel;
    private String memberType;
    private Integer point;
}