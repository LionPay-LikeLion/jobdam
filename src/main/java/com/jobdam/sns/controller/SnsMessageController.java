package com.jobdam.sns.controller;

import com.jobdam.sns.dto.SnsMessageRequestDto;
import com.jobdam.sns.dto.SnsMessageResponseDto;
import com.jobdam.sns.service.SnsMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import com.jobdam.common.util.CustomUserDetails;
import java.util.List;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class SnsMessageController {

    private final SnsMessageService snsMessageService;

    @PostMapping
    public void sendMessage(@AuthenticationPrincipal CustomUserDetails user, @RequestBody SnsMessageRequestDto requestDto) {
        snsMessageService.sendMessage(user.getUserId(), requestDto);
    }

    @GetMapping("/conversation")
    public List<SnsMessageResponseDto> getConversation(
            @AuthenticationPrincipal CustomUserDetails user,
            @RequestParam Integer userId
    ) {
        return snsMessageService.getConversation(user.getUserId(), userId);
    }
}
