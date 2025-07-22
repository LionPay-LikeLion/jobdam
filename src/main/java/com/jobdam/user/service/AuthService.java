package com.jobdam.user.service;

import com.jobdam.common.util.JwtProvider;
import com.jobdam.user.dto.LoginRequestDto;
import com.jobdam.user.dto.LoginResponseDto;
import com.jobdam.user.dto.RegisterRequestDto;
import com.jobdam.user.dto.UserProfileDto;
import com.jobdam.user.entity.User;
import com.jobdam.user.mapper.*;
import com.jobdam.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    public void register(RegisterRequestDto request) {
        if (existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
        } if (existsByNickname(request.getNickname())) {
            throw new IllegalArgumentException("이미 사용 중인 닉네임입니다.");
        }

        String encodedPw = passwordEncoder.encode(request.getPassword());
        System.out.println("비밀번호 인코딩 완료");
        User user = userMapper.toEntity(request, encodedPw);
        System.out.println("유저 정보 매핑 완료");
        userRepository.save(user);
        System.out.println("유저 정보 저장 요청 완료");
    }

    public boolean checkNicknameExists(String nickname) {
        return userRepository.existsByNickname(nickname);
    }

    public boolean checkEmailExists(String email) {
        return userRepository.existsByEmail(email);
    }

    private boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    private boolean existsByNickname(String nickname) {
        return userRepository.existsByNickname(nickname);
    }

    public String login(LoginRequestDto request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 이메일입니다."));

        // 활동 정지 계정 로그인 차단
        if (user.getIsActive() != null && !user.getIsActive()) {
            throw new IllegalArgumentException("탈퇴 또는 정지된 계정입니다. 관리자에게 문의하세요.");
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        System.out.println(">>>>> [Service] 로그인 처리 중, 이메일: " + request.getEmail());

        String roleCode = user.getRoleCode() != null ? user.getRoleCode().getCode() : "USER";
        String token = jwtProvider.createToken(
                user.getUserId(),
                user.getEmail(),
                roleCode
        );
        System.out.println(">>>>> [Service] 생성된 JWT 토큰: " + token);

        return token;
    }


    public LoginResponseDto buildLoginResponse(User user) {
        String accessToken = jwtProvider.createAccessToken(user);
        String refreshToken = jwtProvider.createRefreshToken(user);

        return LoginResponseDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .user(userMapper.toUserProfileDto(user)) // Adjust based on what your frontend expects
                .build();
    }

    public LoginResponseDto loginWithTokens(LoginRequestDto request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 이메일입니다."));

        // 활동 정지 계정 로그인 차단
        if (user.getIsActive() != null && !user.getIsActive()) {
            throw new IllegalArgumentException("탈퇴 또는 정지된 계정입니다. 관리자에게 문의하세요.");
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        log.info(">>>>> [Service] 로그인 처리 중, 이메일: {}", request.getEmail());

        // 역할 코드 안전하게 가져오기
        String roleCode = user.getRoleCode() != null ? user.getRoleCode().getCode() : "USER";
        
        String accessToken = jwtProvider.createToken(user.getUserId(), user.getEmail(), roleCode);
        String refreshToken = jwtProvider.createRefreshToken(user.getUserId(), user.getEmail(), roleCode);

        // UserProfileDto 생성
        UserProfileDto profile = userMapper.toUserProfileDto(user);

        log.info(">>>>> [Service] JWT 토큰 생성 완료");

        return LoginResponseDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .user(profile)
                .build();
    }

}
