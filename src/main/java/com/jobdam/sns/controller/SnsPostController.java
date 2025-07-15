package com.jobdam.sns.controller;

import com.jobdam.sns.dto.SnsPostRequestDto;
import com.jobdam.sns.dto.SnsPostResponseDto;
import com.jobdam.sns.service.SnsPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sns/posts")
@RequiredArgsConstructor
public class SnsPostController {

    private final SnsPostService snsPostService;

    // 전체 게시글 조회
    @GetMapping
    public List<SnsPostResponseDto> getAllPosts(@RequestParam Integer userId) {
        return snsPostService.getAllPosts(userId);
    }

    // 단일 게시글 조회
    @GetMapping("/{postId}")
    public SnsPostResponseDto getPost(@PathVariable Integer postId, @RequestParam Integer userId) {
        return snsPostService.getPostById(postId, userId);
    }

    // 게시글 작성
    @PostMapping
    public Integer createPost(@RequestBody SnsPostRequestDto requestDto, @RequestParam Integer userId) {
        return snsPostService.createPost(requestDto, userId);
    }

    // 게시글 수정
    @PutMapping("/{postId}")
    public void updatePost(@PathVariable Integer postId,
                           @RequestBody SnsPostRequestDto requestDto,
                           @RequestParam Integer userId) {
        snsPostService.updatePost(postId, requestDto, userId);
    }

    // 게시글 삭제
    @DeleteMapping("/{postId}")
    public void deletePost(@PathVariable Integer postId, @RequestParam Integer userId) {
        snsPostService.deletePost(postId, userId);
    }
}
