package com.jobdam.user.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserProfileDto {

    private Integer userId;
    private String email;
    private String nickname;
    private Integer remainingPoints;
    private String subscriptionLevel;
    private String role;
    private String phone;
    private String profileImageUrl;
    private String memberTypeCode;
    private LocalDateTime createdAt;

}

