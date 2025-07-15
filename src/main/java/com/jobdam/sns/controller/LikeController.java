package com.jobdam.sns.controller;

import com.jobdam.sns.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/sns/likes")
@RequiredArgsConstructor
public class LikeController {

    private final LikeService likeService;

    // 좋아요 추가
    @PostMapping
    public void like(@RequestParam Integer userId, @RequestParam Integer postId) {
        likeService.addLike(userId, postId);
    }

    // 좋아요 취소
    @DeleteMapping
    public void unlike(@RequestParam Integer userId, @RequestParam Integer postId) {
        likeService.removeLike(userId, postId);
    }
}

