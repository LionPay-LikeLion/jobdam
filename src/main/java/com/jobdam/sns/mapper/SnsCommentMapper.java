package com.jobdam.sns.mapper;

import com.jobdam.sns.dto.SnsCommentRequestDto;
import com.jobdam.sns.dto.SnsCommentResponseDto;
import com.jobdam.sns.entity.SnsComment;
import com.jobdam.user.entity.User;

import java.time.LocalDateTime;

public class SnsCommentMapper {

    // 요청 DTO → 엔티티
    public static SnsComment toEntity(SnsCommentRequestDto dto, Integer userId) {
        SnsComment comment = new SnsComment();
        comment.setSnsPostId(dto.getSnsPostId());
        comment.setUserId(userId);
        comment.setContent(dto.getContent());
        comment.setCreatedAt(LocalDateTime.now()); // 작성 시간 수동 설정
        return comment;
    }

    // 엔티티 → 응답 DTO
    public static SnsCommentResponseDto toDto(SnsComment entity, User user) {
        SnsCommentResponseDto dto = new SnsCommentResponseDto();
        dto.setSnsCommentId(entity.getSnsCommentId());
        dto.setSnsPostId(entity.getSnsPostId());
        dto.setUserId(entity.getUserId());
        dto.setNickname(user != null ? user.getNickname() : "알 수 없음");
        dto.setContent(entity.getContent());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        return dto;
    }
}
