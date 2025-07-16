package com.jobdam.sns.controller;

import com.jobdam.sns.dto.SnsMessageRequestDto;
import com.jobdam.sns.dto.SnsMessageResponseDto;
import com.jobdam.sns.service.SnsMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class SnsMessageController {

    private final SnsMessageService snsMessageService;

    @PostMapping
    public void sendMessage(@RequestParam Long senderId, @RequestBody SnsMessageRequestDto requestDto) {
        snsMessageService.sendMessage(senderId, requestDto);
    }

    @GetMapping("/conversation")
    public List<SnsMessageResponseDto> getConversation(
            @RequestParam Long userId1,
            @RequestParam Long userId2
    ) {
        return snsMessageService.getConversation(userId1, userId2);
    }
}
