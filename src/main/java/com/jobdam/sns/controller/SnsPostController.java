package com.jobdam.sns.controller;

import com.jobdam.sns.dto.SnsPostRequestDto;
import com.jobdam.sns.dto.SnsPostResponseDto;
import com.jobdam.sns.dto.SnsPostDetailResponseDto;

import com.jobdam.sns.service.SnsPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import com.jobdam.sns.dto.SnsPostFilterDto;

import java.util.List;

@RestController
@RequestMapping("/api/sns/posts")
@RequiredArgsConstructor
public class SnsPostController {

    private final SnsPostService snsPostService;

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
    public List<SnsPostResponseDto> getAllPosts(@RequestParam Integer userId) {
        return snsPostService.getAllPosts(userId);
    }

    // 내 피드(내가 쓴 게시글) 조회
    @GetMapping("/my")
    public List<SnsPostResponseDto> getMyPosts(@RequestParam Integer userId) {
        return snsPostService.getMyPosts(userId);
    }


    @GetMapping("/{postId}")
    public SnsPostDetailResponseDto getPostDetail(@PathVariable Integer postId,
                                                  @RequestParam Integer userId) {
        return snsPostService.getPostDetail(postId, userId);
    }



    @PostMapping
    public Integer createPost(@RequestBody SnsPostRequestDto requestDto, @RequestParam Integer userId) {
        return snsPostService.createPost(requestDto, userId);
    }

    @PutMapping("/{postId}")
    public void updatePost(@PathVariable Integer postId,
                           @RequestBody SnsPostRequestDto requestDto,
                           @RequestParam Integer userId) {
        snsPostService.updatePost(postId, requestDto, userId);
    }

    @DeleteMapping("/{postId}")
    public void deletePost(@PathVariable Integer postId, @RequestParam Integer userId) {
        snsPostService.deletePost(postId, userId);
    }
}
