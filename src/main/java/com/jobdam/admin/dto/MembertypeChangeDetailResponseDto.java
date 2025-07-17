package com.jobdam.admin.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class MembertypeChangeDetailResponseDto {

    private Integer requestId;
    private String userEmail;
    private String userNickname;
    private String currentMemberTypeCode;
    private String requestedMemberTypeCode;
    private String title;
    private String reason;
    private String content;
    private String referenceLink;
    private String attachmentUrl;
    private String requestStatus;
    private LocalDateTime requestedAt;
    private LocalDateTime processedAt;
}
