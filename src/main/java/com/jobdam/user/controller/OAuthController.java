package com.jobdam.user.controller;

import com.jobdam.code.repository.RoleCodeRepository;
import com.jobdam.user.dto.OAuthRegisterRequestDto;
import com.jobdam.user.entity.User;
import com.jobdam.common.util.JwtProvider;
import com.jobdam.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.jobdam.common.util.GoogleVerifier;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import java.util.Collections;
import java.util.Map;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.*;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import java.util.HashMap;
import org.springframework.beans.factory.annotation.Value;

@Slf4j
@RestController
@RequestMapping("/api/oauth")
@RequiredArgsConstructor
public class OAuthController {

    @Value("${GOOGLE_CLIENT_ID}")
    private String googleClientId;

    @Value("${GOOGLE_CLIENT_SECRET}")
    private String googleClientSecret;

    @Value("${GOOGLE_REDIRECT_URI}")
    private String googleRedirectUri;

    private final UserService userService;
    private final JwtProvider jwtProvider;
    private final RoleCodeRepository roleCodeRepository;
    private final GoogleVerifier googleVerifier;

    @PostMapping("/login")
    public ResponseEntity<String> oauthLogin(@RequestBody Map<String, String> body) {
        String code = body.get("code");
        log.info("✅ Received Google authorization code: {}", (code != null ? "PRESENT" : "MISSING"));

        try {
            String idToken = exchangeCodeForIdToken(code);
            log.info("✅ Received ID Token from Google");

            GoogleIdToken.Payload payload = verifyGoogleIdToken(idToken);
            if (payload == null || !Boolean.TRUE.equals(payload.getEmailVerified())) {
                log.warn("❌ Google ID token verification failed or email not verified");
                return ResponseEntity.status(401).build();
            }

            OAuthRegisterRequestDto requestDto = buildOAuthRegisterDto(payload);
            log.info("✅ OAuth DTO built for email: {}", requestDto.getEmail());

            User user = userService.findOrRegisterOAuthUser(requestDto);
            log.info("✅ User registered or retrieved: {}", user.getEmail());

            String token = issueJwtToken(user);
            log.info("✅ JWT token issued for userId: {}", user.getUserId());

            return ResponseEntity.ok(token);

        } catch (Exception e) {
            log.error("❌ Google login process failed: {}", e.getMessage(), e);
            return ResponseEntity.status(500).body("Google 로그인 처리 중 오류가 발생했습니다.");
        }
    }

    private String exchangeCodeForIdToken(String code) throws Exception {
        String clientId = googleClientId;
        String clientSecret = googleClientSecret;
        String redirectUri = googleRedirectUri;

        log.info("🔁 Exchanging code for ID token with Google");
        log.debug("🔐 client_id: {}", clientId);
        log.debug("🔐 client_secret present: {}", clientSecret != null);
        log.debug("🔐 redirect_uri: {}", redirectUri);

        HttpRequestFactory requestFactory = GoogleNetHttpTransport.newTrustedTransport()
                .createRequestFactory(request -> {});
        GenericUrl tokenUrl = new GenericUrl("https://oauth2.googleapis.com/token");

        Map<String, String> params = new HashMap<>();
        params.put("code", code);
        params.put("client_id", clientId);
        params.put("client_secret", clientSecret);
        params.put("redirect_uri", redirectUri);
        params.put("grant_type", "authorization_code");

        UrlEncodedContent content = new UrlEncodedContent(params);
        HttpRequest request = requestFactory.buildPostRequest(tokenUrl, content);

        HttpResponse response = request.execute();
        Map<String, Object> tokenResponse = new ObjectMapper().readValue(response.getContent(), Map.class);

        log.debug("📦 Token response from Google: {}", tokenResponse);

        return (String) tokenResponse.get("id_token");
    }

    private GoogleIdToken.Payload verifyGoogleIdToken(String idToken) throws Exception {
        log.info("🔍 Verifying Google ID Token...");
        NetHttpTransport transport = GoogleNetHttpTransport.newTrustedTransport();
        JacksonFactory jsonFactory = JacksonFactory.getDefaultInstance();

        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(transport, jsonFactory)
                .setAudience(Collections.singletonList(googleClientId))
                .build();

        GoogleIdToken idTokenObj = verifier.verify(idToken);
        if (idTokenObj == null) {
            log.warn("❌ ID token verification failed (null response)");
        }

        return (idTokenObj != null) ? idTokenObj.getPayload() : null;
    }

    private OAuthRegisterRequestDto buildOAuthRegisterDto(GoogleIdToken.Payload payload) {
        OAuthRegisterRequestDto dto = new OAuthRegisterRequestDto();
        dto.setEmail(payload.getEmail());
        dto.setNickname((String) payload.get("name"));
        dto.setProviderId(payload.getSubject());
        dto.setProviderType("GOOGLE");
        dto.setEmailVerified(payload.getEmailVerified());
        dto.setProfileImageUrl((String) payload.get("picture"));
        return dto;
    }

    private String issueJwtToken(User user) {
        String roleCode = roleCodeRepository.findById(user.getRoleCodeId())
                .map(role -> role.getCode())
                .orElse("USER");

        log.debug("🛡 Issuing JWT with role: {}", roleCode);
        return jwtProvider.createToken(user.getUserId(), user.getEmail(), roleCode);
    }
}