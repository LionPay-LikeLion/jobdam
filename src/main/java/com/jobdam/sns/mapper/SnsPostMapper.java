package com.jobdam.sns.mapper;

import com.jobdam.sns.dto.SnsPostRequestDto;
import com.jobdam.sns.dto.SnsPostResponseDto;
import com.jobdam.sns.entity.SnsPost;
import com.jobdam.user.entity.User;

public class SnsPostMapper {

    public static SnsPost toEntity(SnsPostRequestDto dto, Integer userId) {
        SnsPost post = new SnsPost();
        post.setUserId(userId);
        post.setTitle(dto.getTitle());
        post.setContent(dto.getContent());
        post.setImageUrl(dto.getImageUrl());
        post.setAttachmentUrl(dto.getAttachmentUrl());
        return post;
    }


    public static SnsPostResponseDto toDto(SnsPost post, User user, int likeCount, int commentCount, boolean isLiked, boolean isBookmarked) {
        SnsPostResponseDto dto = new SnsPostResponseDto();
        dto.setSnsPostId(post.getSnsPostId());
        dto.setUserId(post.getUserId());
        dto.setNickname(user != null ? user.getNickname() : "알 수 없음");
        dto.setTitle(post.getTitle());
        dto.setContent(post.getContent());
        dto.setImageUrl(post.getImageUrl());
        dto.setAttachmentUrl(post.getAttachmentUrl());
        dto.setCreatedAt(post.getCreatedAt());
        dto.setLikeCount(likeCount);
        dto.setCommentCount(commentCount);
        dto.setLiked(isLiked);
        dto.setBookmarked(isBookmarked);

        dto.setMemberTypeCode(user != null && user.getMemberTypeCode() != null
                ? user.getMemberTypeCode().getCode()
                : null);

        return dto;
    }
}
