package com.jobdam.sns.controller;

import com.jobdam.common.util.CustomUserDetails;
import com.jobdam.sns.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/sns/likes")
@RequiredArgsConstructor
public class LikeController {

    private final LikeService likeService;


    @PostMapping
    public void like(@AuthenticationPrincipal CustomUserDetails user, @RequestParam Integer postId) {
        likeService.addLike(user.getUserId(), postId);
    }

    @DeleteMapping
    public void unlike(@AuthenticationPrincipal CustomUserDetails user, @RequestParam Integer postId) {
        likeService.removeLike(user.getUserId(), postId);
    }
}

