package com.jobdam.user.controller;

import com.jobdam.code.repository.RoleCodeRepository;
import com.jobdam.user.dto.OAuthRegisterRequestDto;
import com.jobdam.user.entity.User;
import com.jobdam.common.util.JwtProvider;
import com.jobdam.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.jobdam.common.util.GoogleVerifier;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import java.util.Map;

@RestController
@RequestMapping("/api/oauth")
@RequiredArgsConstructor
public class OAuthController {

    private final UserService userService;
    private final JwtProvider jwtProvider;
    private final RoleCodeRepository roleCodeRepository;
    private final GoogleVerifier googleVerifier;

    @PostMapping("/login")
    public ResponseEntity<String> oauthLogin(@RequestBody Map<String, String> body) {
        String credential = body.get("credential");
        GoogleIdToken.Payload payload = googleVerifier.verify(credential);
        if (payload == null || !Boolean.TRUE.equals(payload.getEmailVerified())) {
            return ResponseEntity.status(401).build();
        }

        String email = payload.getEmail();
        String name = (String) payload.get("name");

        OAuthRegisterRequestDto requestDto = new OAuthRegisterRequestDto();
        requestDto.setEmail(email);
        requestDto.setNickname(name);

        User user = userService.findOrRegisterOAuthUser(requestDto);

        String roleCode = roleCodeRepository.findById(user.getRoleCodeId())
                .map(role -> role.getCode())
                .orElse("USER");

        String token = jwtProvider.createToken(
                user.getUserId(),
                user.getEmail(),
                roleCode
        );
        return ResponseEntity.ok(token);
    }
}