package com.jobdam.user.controller;

import com.jobdam.code.repository.RoleCodeRepository;
import com.jobdam.user.dto.OAuthRegisterRequestDto;
import com.jobdam.user.entity.User;
import com.jobdam.common.util.JwtProvider;
import com.jobdam.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/oauth")
@RequiredArgsConstructor
public class OAuthController {

    private final UserService userService;
    private final JwtProvider jwtProvider;
    private final RoleCodeRepository roleCodeRepository;

    @PostMapping("/login")
    public ResponseEntity<String> oauthLogin(@RequestBody OAuthRegisterRequestDto requestDto) {
        // register if new, or fetch existing
        User user = userService.findOrRegisterOAuthUser(requestDto);

        // get role code string safely
        String roleCode = roleCodeRepository.findById(user.getRoleCodeId())
                .map(role -> role.getCode())
                .orElse("USER");  // fallback default

        // issue JWT
        String token = jwtProvider.createToken(
                user.getUserId(),
                user.getEmail(),
                roleCode
        );
        return ResponseEntity.ok(token);
    }
}