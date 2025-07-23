package com.jobdam.admin.service;

import com.jobdam.user.entity.User;
import com.jobdam.user.repository.UserRepository;
import com.jobdam.admin.dto.UserResponseDto;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminUserService {

    private final UserRepository userRepository;

    // 전체 회원 조회 (필요시 페이징으로 변경)
    public List<UserResponseDto> getAllUsers() {
        List<User> userList = userRepository.findAll();
        return userList.stream().map(this::toDto).collect(Collectors.toList());
    }

    @Transactional
    public void activateUser(Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("해당 회원이 존재하지 않습니다."));
        user.setIsActive(true);
        userRepository.save(user);
    }

    @Transactional
    public void deactivateUser(Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("해당 회원이 존재하지 않습니다."));
        user.setIsActive(false);
        userRepository.save(user);
    }

    private UserResponseDto toDto(User user) {
        UserResponseDto dto = new UserResponseDto();
        dto.setUserId(user.getUserId());
        dto.setEmail(user.getEmail());
        dto.setNickname(user.getNickname());
        dto.setIsActive(user.getIsActive());
        dto.setRoleCodeId(user.getRoleCodeId());
        return dto;
    }

    @Transactional
    public void grantAdmin(Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("해당 회원이 존재하지 않습니다."));
        user.setRoleCodeId(2); // 2번: 관리자
        userRepository.save(user);
    }

    @Transactional
    public void revokeAdmin(Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("해당 회원이 존재하지 않습니다."));
        user.setRoleCodeId(1); // 1번: 일반유저
        userRepository.save(user);
    }

}
