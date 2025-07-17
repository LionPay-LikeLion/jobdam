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
    private String userNickname;
    private String currentMemberTypeCode;
    private String requestedMemberTypeCode;
    private String title;
    private String requestStatus;
    private LocalDateTime requestedAt;

}