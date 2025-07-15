package com.jobdam.sns.service;

import com.jobdam.sns.dto.SnsPostRequestDto;
import com.jobdam.sns.dto.SnsPostResponseDto;

import java.util.List;

public interface SnsPostService {

    /**
     * 전체 SNS 게시글 목록 조회
     * @param currentUserId 현재 로그인한 사용자 ID (좋아요/북마크 여부 확인용)
     * @return 게시글 목록 DTO 리스트
     */
    List<SnsPostResponseDto> getAllPosts(Integer currentUserId);

    /**
     * 단일 게시글 상세 조회
     * @param postId 조회할 게시글 ID
     * @param currentUserId 현재 로그인한 사용자 ID
     * @return 게시글 상세 정보 DTO
     */
    SnsPostResponseDto getPostById(Integer postId, Integer currentUserId);

    /**
     * 게시글 작성
     * @param requestDto 작성할 게시글 내용 DTO
     * @param userId 작성자 ID
     * @return 생성된 게시글 ID
     */
    Integer createPost(SnsPostRequestDto requestDto, Integer userId);

    /**
     * 게시글 수정
     * @param postId 수정할 게시글 ID
     * @param requestDto 수정할 내용
     * @param userId 요청자 ID (작성자 본인 확인용)
     */
    void updatePost(Integer postId, SnsPostRequestDto requestDto, Integer userId);

    /**
     * 게시글 삭제
     * @param postId 삭제할 게시글 ID
     * @param userId 요청자 ID (작성자 본인 확인용)
     */
    void deletePost(Integer postId, Integer userId);
}
