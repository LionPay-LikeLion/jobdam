package com.jobdam.common.filter;

import com.jobdam.common.util.JwtProvider;
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
                String email = claims.getSubject();
                Integer roleId = Integer.valueOf(claims.get("role").toString());

                String roleName = "ROLE_" + roleCodeService.getRoleNameById(roleId);
                SimpleGrantedAuthority authority = new SimpleGrantedAuthority(roleName);

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(email, null, Collections.singletonList(authority));

                SecurityContextHolder.getContext().setAuthentication(authentication);

            } catch (JwtException | IllegalArgumentException e) {
                System.out.println(">>>>> [JwtFilter] 예외 발생: " + e.getClass().getSimpleName() + " - " + e.getMessage());
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid JWT token");
                return;
            }
        }

        filterChain.doFilter(request, response);
    }
}
