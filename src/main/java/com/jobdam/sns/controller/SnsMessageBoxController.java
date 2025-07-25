package com.jobdam.sns.controller;

import com.jobdam.common.util.CustomUserDetails;
import com.jobdam.sns.dto.SnsMessageBoxResponseDto;
import com.jobdam.sns.service.SnsMessageBoxService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class SnsMessageBoxController {

    private final SnsMessageBoxService snsMessageBoxService;

    @GetMapping("/boxes")
    public List<SnsMessageBoxResponseDto> getMessageBoxes(@AuthenticationPrincipal CustomUserDetails user) {
        return snsMessageBoxService.getMessageBoxes(user.getUserId());
    }
}
