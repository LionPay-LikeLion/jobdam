package com.jobdam.sns.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class SnsMessageResponseDto {
    private Long messageId;
    private Long senderId;
    private Long receiverId;
    private String content;
    private LocalDateTime createdAt;
}
