package com.jobdam.sns.dto;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class SnsMessageBoxResponseDto {

    private Long opponentUserId;
    private String opponentNickname;
    private String opponentProfileImageUrl;
    private String lastMessageContent;
    private LocalDateTime lastMessageCreatedAt;

    public SnsMessageBoxResponseDto(
            Long opponentUserId,
            String opponentNickname,
            String opponentProfileImageUrl,
            String lastMessageContent,
            LocalDateTime lastMessageCreatedAt
    ) {
        this.opponentUserId = opponentUserId;
        this.opponentNickname = opponentNickname;
        this.opponentProfileImageUrl = opponentProfileImageUrl;
        this.lastMessageContent = lastMessageContent;
        this.lastMessageCreatedAt = lastMessageCreatedAt;
    }

    public SnsMessageBoxResponseDto() {
    }
}
