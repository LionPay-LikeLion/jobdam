package com.jobdam.sns.mapper;

import com.jobdam.sns.dto.SnsCommentRequestDto;
import com.jobdam.sns.dto.SnsCommentResponseDto;
import com.jobdam.sns.entity.SnsComment;
import com.jobdam.user.entity.User;

import java.time.LocalDateTime;

public class SnsCommentMapper {

    public static SnsComment toEntity(SnsCommentRequestDto dto, Integer userId) {
        SnsComment comment = new SnsComment();
        comment.setSnsPostId(dto.getSnsPostId());
        comment.setUserId(userId);
        comment.setContent(dto.getContent());
        comment.setCreatedAt(LocalDateTime.now());
        return comment;
    }


    public static SnsCommentResponseDto toDto(SnsComment entity, User user) {
        SnsCommentResponseDto dto = new SnsCommentResponseDto();
        dto.setSnsCommentId(entity.getSnsCommentId());
        dto.setSnsPostId(entity.getSnsPostId());
        dto.setUserId(entity.getUserId());
        dto.setNickname(user != null ? user.getNickname() : "알 수 없음");
        dto.setContent(entity.getContent());

        dto.setProfileImageUrl(
                user != null ? user.getProfileImageUrl() : null
        );

        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        dto.setBoardStatusCode(
                entity.getBoardStatusCode() != null
                        ? entity.getBoardStatusCode().getCode()
                        : null
        );

        return dto;
    }
}
