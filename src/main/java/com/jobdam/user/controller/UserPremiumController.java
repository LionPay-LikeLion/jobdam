package com.jobdam.user.controller;

import com.jobdam.user.dto.UserPremiumRequest;
import com.jobdam.user.service.UserPremiumService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user/subscription")
@RequiredArgsConstructor
public class UserPremiumController {

    private final UserPremiumService upgradeService;

    @PostMapping("/upgrade")
    public ResponseEntity<String> upgrade(@RequestBody UserPremiumRequest request) {
        upgradeService.upgradeToPremium(request);
        return ResponseEntity.ok("프리미엄 회원 업그레이드가 완료되었습니다.");
    }
}
