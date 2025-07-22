package com.jobdam.admin.controller;

import com.jobdam.admin.dto.UserResponseDto;
import com.jobdam.admin.service.AdminUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/user")
@RequiredArgsConstructor
public class AdminUserController {

    private final AdminUserService adminUserService;

    // 전체 회원 목록
    @GetMapping
    public List<UserResponseDto> getUsers() {
        return adminUserService.getAllUsers();
    }

    // 회원 활성화
    @PatchMapping("/{id}/activate")
    public void activateUser(@PathVariable Integer id) {
        adminUserService.activateUser(id);
    }

    // 회원 정지
    @PatchMapping("/{id}/deactivate")
    public void deactivateUser(@PathVariable Integer id) {
        adminUserService.deactivateUser(id);
    }


    // 관리자 권한 부여
    @PatchMapping("/{id}/grant-admin")
    public void grantAdmin(@PathVariable Integer id) {
        adminUserService.grantAdmin(id);
    }

    // 관리자 권한 해제
    @PatchMapping("/{id}/revoke-admin")
    public void revokeAdmin(@PathVariable Integer id) {
        adminUserService.revokeAdmin(id);
    }
}
