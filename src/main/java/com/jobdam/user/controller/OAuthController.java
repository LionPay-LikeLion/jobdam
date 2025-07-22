package com.jobdam.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.*;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.jobdam.user.dto.LoginResponseDto;
import com.jobdam.user.dto.OAuthRegisterRequestDto;
import com.jobdam.user.entity.User;
import com.jobdam.user.service.AuthService;
import com.jobdam.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

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
    private final AuthService authService;


    @PostMapping("/login")
    public ResponseEntity<?> oauthLogin(@RequestBody Map<String, String> body) {
        String code = body.get("code");
        log.info("✅ Received Google authorization code: {}", (code != null ? "PRESENT" : "MISSING"));

        try {
            String idToken = exchangeCodeForIdToken(code);
            log.info("✅ Received ID Token from Google");

            GoogleIdToken.Payload payload = verifyGoogleIdToken(idToken);
            if (payload == null || !Boolean.TRUE.equals(payload.getEmailVerified())) {
                log.warn("❌ Google ID token verification failed or email not verified");
                return ResponseEntity.status(401).body(Map.of("error", "Google ID token verification failed"));
            }

            OAuthRegisterRequestDto requestDto = buildOAuthRegisterDto(payload);
            log.info("✅ OAuth DTO built for email: {}", requestDto.getEmail());

            User user = userService.findOrRegisterOAuthUser(requestDto);
            log.info("✅ User registered or retrieved: {}", user.getEmail());

            LoginResponseDto response = userService.buildLoginResponse(user);
            log.info("✅ JWT tokens issued and LoginResponseDto built");

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("❌ Google login process failed: {}", e.getMessage(), e);
            return ResponseEntity.status(500).body(Map.of("error", "Google 로그인 처리 중 오류가 발생했습니다."));
        }
    }

    private String exchangeCodeForIdToken(String code) throws Exception {
        log.info("🔁 Exchanging code for ID token with Google");

        HttpRequestFactory requestFactory = GoogleNetHttpTransport.newTrustedTransport()
                .createRequestFactory(request -> request.setParser(GsonFactory.getDefaultInstance().createJsonObjectParser()));

        GenericUrl tokenUrl = new GenericUrl("https://oauth2.googleapis.com/token");

        Map<String, String> params = new HashMap<>();
        params.put("code", code);
        params.put("client_id", googleClientId);
        params.put("client_secret", googleClientSecret);
        params.put("redirect_uri", googleRedirectUri);
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
        GsonFactory jsonFactory = GsonFactory.getDefaultInstance();

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
        return OAuthRegisterRequestDto.builder()
                .email(payload.getEmail())
                .nickname((String) payload.get("name"))
                .providerId(payload.getSubject())
                .providerType("GOOGLE")
                .emailVerified(payload.getEmailVerified())
                .profileImageUrl((String) payload.get("picture"))
                .build();
    }
}