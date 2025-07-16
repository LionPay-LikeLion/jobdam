package com.jobdam.sns.mapper;

import com.jobdam.sns.dto.SnsMessageResponseDto;
import com.jobdam.sns.entity.SnsMessage;

public class SnsMessageMapper {

    public static SnsMessageResponseDto toDto(SnsMessage message) {
        return SnsMessageResponseDto.builder()
                .messageId(message.getMessageId())
                .senderId(message.getSenderId())
                .receiverId(message.getReceiverId())
                .content(message.getContent())
                .createdAt(message.getCreatedAt())
                .build();
    }
}
