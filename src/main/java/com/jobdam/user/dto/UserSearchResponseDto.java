package com.jobdam.user.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserSearchResponseDto {

    private Integer userId;
    private String nickname;
    private Integer remainingPoints;
    private String subscriptionLevelCode;
    private String profileImageUrl;
    private String memberTypeCode;

}
