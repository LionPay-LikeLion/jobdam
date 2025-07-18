package com.jobdam.common.util;

import lombok.Builder;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@Getter
public class CustomUserDetails implements UserDetails {

    private final Integer userId;
    private final String email;
    private final String nickname;
    private final String roleCode;
    private final String subscriptionLevelCode;
    private final String memberTypeCode;

    @Builder
    public CustomUserDetails(Integer userId, String email, String nickname,
                             String roleCode, String subscriptionLevelCode, String memberTypeCode) {
        this.userId = userId;
        this.email = email;
        this.nickname = nickname;
        this.roleCode = roleCode;
        this.subscriptionLevelCode = subscriptionLevelCode;
        this.memberTypeCode = memberTypeCode;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(() -> "ROLE_" + roleCode); // 권한 부여
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
