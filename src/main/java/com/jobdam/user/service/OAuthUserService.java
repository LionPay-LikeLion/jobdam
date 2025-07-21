package com.jobdam.user.service;

import com.jobdam.user.dto.OAuthRegisterRequestDto;
import com.jobdam.user.entity.User;
import com.jobdam.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OAuthUserService {

    private final UserRepository userRepository;

    public User registerOAuthUser(OAuthRegisterRequestDto dto) {
        // does user already exist (providerId or by email as fallback)
        Optional<User> existing = userRepository.findByProviderId(dto.getProviderId());
        if (existing.isPresent()) {
            return existing.get();  // or throw if you want to enforce single registration
        }

        // build new user
        User newUser = User.builder()
                .providerId(dto.getProviderId())
                .providerType(dto.getProviderType())
                .email(dto.getEmail())
                .emailVerified(dto.getEmailVerified())
                .nickname(dto.getNickname())
                .profileImageUrl(dto.getProfileImageUrl())
                .subscriptionLevelCodeId(1)     // default
                .roleCodeId(1)                  // default
                .memberTypeCodeId(1)            // default
                .point(0)
                .build();

        // save and return
        return userRepository.save(newUser);
    }

    public User findOrRegisterOAuthUser(OAuthRegisterRequestDto dto) {
        if (!dto.getEmailVerified()) {
            throw new IllegalStateException("Google has not verified your email. Please verify it before logging in.");
        }
        // Check if user exists by provider ID
        Optional<User> existingUser = userRepository.findByProviderId(dto.getProviderId());
        return existingUser.orElseGet(() -> registerOAuthUser(dto));
    }

}
