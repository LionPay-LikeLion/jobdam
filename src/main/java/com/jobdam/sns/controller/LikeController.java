package com.jobdam.sns.controller;

import com.jobdam.sns.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/sns/likes")
@RequiredArgsConstructor
public class LikeController {

    private final LikeService likeService;


    @PostMapping
    public void like(@RequestParam Integer userId, @RequestParam Integer postId) {
        likeService.addLike(userId, postId);
    }

    @DeleteMapping
    public void unlike(@RequestParam Integer userId, @RequestParam Integer postId) {
        likeService.removeLike(userId, postId);
    }
}

