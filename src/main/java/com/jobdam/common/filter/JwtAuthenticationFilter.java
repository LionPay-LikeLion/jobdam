package com.jobdam.common.filter;

import com.jobdam.code.repository.MemberTypeCodeRepository;
import com.jobdam.code.repository.RoleCodeRepository;
import com.jobdam.code.repository.SubscriptionLevelCodeRepository;
import com.jobdam.common.util.CustomUserDetails;
import com.jobdam.common.util.JwtProvider;
import com.jobdam.user.entity.User;
import com.jobdam.user.repository.UserRepository;
import com.jobdam.user.service.RoleCodeService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;
    private final RoleCodeService roleCodeService;
    private final UserRepository userRepository;
    private final RoleCodeRepository roleCodeRepository;
    private final MemberTypeCodeRepository memberTypeCodeRepository;
    private final SubscriptionLevelCodeRepository subscriptionLevelCodeRepository;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();
        boolean skip = path.startsWith("/api/auth") || path.startsWith("/swagger") || path.startsWith("/v3");
        // 임시로 적어두었습니다. 필요한 부분 추가 필요합니다.
        System.out.println(">>>>> [JwtFilter] shouldNotFilter() - " + path + " -> " + skip);
        return skip;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getServletPath();
        System.out.println(">>>>> [JwtFilter] 필터 실행됨 - 요청 경로: " + path);

        String authHeader = request.getHeader("Authorization");
        System.out.println(">>>>> [JwtFilter] Authorization 헤더: " + authHeader);

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);

            try {
                Claims claims = jwtProvider.parseClaims(token);
                Integer userId = Integer.parseInt(claims.getSubject());
                String email = claims.get("email", String.class);
                String role = claims.get("role", String.class);

                User user = userRepository.findById(userId)
                        .orElseThrow(() -> new RuntimeException("유저 없음"));

                String subscriptionCode = user.getSubscriptionLevelCodeId() != null
                        ? subscriptionLevelCodeRepository.findById(user.getSubscriptionLevelCodeId())
                        .map(c -> c.getCode())
                        .orElse(null)
                        : null;

                String memberTypeCode = user.getMemberTypeCodeId() != null
                        ? memberTypeCodeRepository.findById(user.getMemberTypeCodeId())
                        .map(c -> c.getCode())
                        .orElse(null)
                        : null;

                CustomUserDetails userDetails = CustomUserDetails.builder()
                        .userId(user.getUserId())
                        .email(email)
                        .nickname(user.getNickname())
                        .roleCode(role)
                        .subscriptionLevelCode(subscriptionCode)
                        .memberTypeCode(memberTypeCode)
                        .build();

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(userDetails, null,
                                Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role)));

                SecurityContextHolder.getContext().setAuthentication(authentication);

            } catch (Exception e) {
                System.out.println(">>>>> [JwtFilter] 예외 발생: " + e.getClass().getSimpleName() + " - " + e.getMessage());
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid JWT token");
                return;
            }
        }

        filterChain.doFilter(request, response);
    }
}
