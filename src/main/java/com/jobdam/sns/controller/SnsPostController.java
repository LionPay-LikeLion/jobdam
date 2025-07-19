package com.jobdam.sns.controller;

import com.jobdam.sns.dto.SnsPostRequestDto;
import com.jobdam.sns.dto.SnsPostResponseDto;
import com.jobdam.sns.dto.SnsPostDetailResponseDto;

import com.jobdam.sns.service.SnsPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import com.jobdam.sns.dto.SnsPostFilterDto;

import java.util.List;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import com.jobdam.common.util.CustomUserDetails;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/api/sns/posts")
@RequiredArgsConstructor
public class SnsPostController {

    private final SnsPostService snsPostService;

    @Operation(
            summary = "SNS 게시글 키워드 검색",
            description = "제목 또는 본문 내용에 포함된 텍스트로 게시글을 검색합니다."
    )
    @GetMapping("/search")
    public ResponseEntity<List<SnsPostResponseDto>> searchPosts(
            @Parameter(description = "검색 키워드 (제목 또는 본문 기준)") @RequestParam String keyword,
            @RequestParam(required = false) Integer userId
    ) {
        List<SnsPostResponseDto> results = snsPostService.searchPostsByKeyword(keyword, userId);
        return ResponseEntity.ok(results);
    }



    // 필터 기반 게시글 조회 (작성자 유형 + 정렬 기준)

    @GetMapping("/filter")
    public List<SnsPostResponseDto> getFilteredPosts(
            @ModelAttribute SnsPostFilterDto filter,
            @RequestParam Integer userId
    ) {
        return snsPostService.getFilteredPosts(filter, userId);
    }


    // 전체 게시글 조회
    @GetMapping
    public List<SnsPostResponseDto> getAllPosts(@AuthenticationPrincipal CustomUserDetails user) {
        return snsPostService.getAllPosts(user.getUserId());
    }

    // 내 피드(내가 쓴 게시글) 조회
    @GetMapping("/my")
    public List<SnsPostResponseDto> getMyPosts(@AuthenticationPrincipal CustomUserDetails user) {
        return snsPostService.getMyPosts(user.getUserId());
    }

    // 게시글 상세 조회 (로그인 유저가 좋아요 했는지 등 확인)
    @GetMapping("/{postId}")
    public SnsPostDetailResponseDto getPostDetail(@PathVariable Integer postId,
                                                  @AuthenticationPrincipal CustomUserDetails user) {
        return snsPostService.getPostDetail(postId, user.getUserId());
    }


    // 게시글 작성
    @PostMapping
    public Integer createPost(@ModelAttribute SnsPostRequestDto requestDto, @AuthenticationPrincipal CustomUserDetails user) {
        return snsPostService.createPost(requestDto, user.getUserId());
    }

    // 게시글 수정
    @PutMapping("/{postId}")
    public void updatePost(@PathVariable Integer postId,
                           @ModelAttribute SnsPostRequestDto requestDto,
                           @AuthenticationPrincipal CustomUserDetails user) {
        snsPostService.updatePost(postId, requestDto, user.getUserId());
    }

    // 게시글 삭제
    @DeleteMapping("/{postId}")
    public void deletePost(@PathVariable Integer postId, @AuthenticationPrincipal CustomUserDetails user) {
        snsPostService.deletePost(postId, user.getUserId());
    }

    @Operation(
            summary = "프리미엄 우선 피드 조회",
            description = "프리미엄 회원의 게시글이 먼저 노출되도록 정렬된 피드를 조회합니다."
    )
    @GetMapping("/premium-priority")
    public ResponseEntity<List<SnsPostResponseDto>> getPremiumPriorityFeed(@AuthenticationPrincipal CustomUserDetails user) {
        List<SnsPostResponseDto> posts = snsPostService.getPremiumPriorityPosts(user.getUserId());
        return ResponseEntity.ok(posts);
    }

}
