package com.jobdam.user.mapper;

import com.jobdam.user.dto.RegisterRequestDto;
import com.jobdam.user.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User toEntity(RegisterRequestDto request, String encodedPassword) {
        return new User(
                0,
                request.getEmail(),
                encodedPassword,
                request.getNickname(),
                1,
                1,
                1,
                null,
                0,
                request.getPhone(),
                request.getProfileImageUrl()
        );
    }
}
