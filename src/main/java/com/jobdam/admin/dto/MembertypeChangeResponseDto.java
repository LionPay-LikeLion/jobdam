package com.jobdam.admin.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MembertypeChangeResponseDto {

    private Integer requestId;
    private String userEmail;
    private String userNickname; // == 이름(없으면 별도로 user.getName() 추가)
    private String userName;     // 유저 이름(신규 추가)
    private String currentMemberTypeCode;
    private String requestedMemberTypeCode;
    private String requestedMemberTypeName; // 희망 역할 한글명(EMPLOYEE → 기업회원 등)
    private String title;
    private String requestStatus;
    private Integer requestStatusCode; // 0: 대기중, 1: 거절, 2: 승인
    private String attachmentUrl;
    private java.time.LocalDateTime requestedAt;
}
