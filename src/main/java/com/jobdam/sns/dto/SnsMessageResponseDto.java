package com.jobdam.sns.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class SnsMessageResponseDto {
    private Integer messageId;
    private Integer senderId;
    private Integer receiverId;
    private String content;
    private LocalDateTime createdAt;

    private String senderNickname;
    private String senderProfileImageUrl;
}
