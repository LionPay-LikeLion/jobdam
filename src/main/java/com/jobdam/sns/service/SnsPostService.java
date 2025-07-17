package com.jobdam.sns.service;

import com.jobdam.sns.dto.SnsPostFilterDto;
import com.jobdam.sns.dto.SnsPostRequestDto;
import com.jobdam.sns.dto.SnsPostResponseDto;
import com.jobdam.sns.dto.SnsPostDetailResponseDto;
import java.util.List;

public interface SnsPostService {

    List<SnsPostResponseDto> getAllPosts(Integer currentUserId);
    SnsPostResponseDto getPostById(Integer postId, Integer currentUserId);
    SnsPostDetailResponseDto getPostDetail(Integer postId, Integer userId);

    Integer createPost(SnsPostRequestDto requestDto, Integer userId);

    void updatePost(Integer postId, SnsPostRequestDto requestDto, Integer userId);
    void deletePost(Integer postId, Integer userId);
    List<SnsPostResponseDto> getFilteredPosts(SnsPostFilterDto filter, Integer currentUserId);

    List<SnsPostResponseDto> getMyPosts(Integer currentUserId);

    List<SnsPostResponseDto> searchPostsByKeyword(String keyword, Integer userId);

    List<SnsPostResponseDto> getPremiumPriorityPosts(Integer currentUserId);

}
