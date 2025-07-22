package com.jobdam.sns.mapper;

import com.jobdam.sns.dto.SnsPostRequestDto;
import com.jobdam.sns.dto.SnsPostResponseDto;
import com.jobdam.sns.entity.SnsPost;
import com.jobdam.user.entity.User;

public class SnsPostMapper {

    public static SnsPost toEntity(SnsPostRequestDto dto, Integer userId, String imageUrl, String attachmentUrl) {
        SnsPost post = new SnsPost();
        post.setUserId(userId);
        post.setTitle(dto.getTitle());
        post.setContent(dto.getContent());
        post.setImageUrl(imageUrl); // 저장된 파일 경로/URL
        post.setAttachmentUrl(attachmentUrl); // 저장된 파일 경로/URL
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

        dto.setProfileImageUrl(
                user != null ? user.getProfileImageUrl() : null
        );

        dto.setSubscriptionLevelCode(
                user != null && user.getSubscriptionLevelCode() != null
                        ? user.getSubscriptionLevelCode().getCode()
                        : null
        );

        dto.setMemberTypeCode(user != null && user.getMemberTypeCode() != null
                ? user.getMemberTypeCode().getCode()
                : null);

        dto.setBoardStatusCode(
                post.getBoardStatusCode() != null
                        ? post.getBoardStatusCode().getCode()
                        : null);

        return dto;
    }
}
