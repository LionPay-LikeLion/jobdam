package com.jobdam.admin.dto;

import lombok.Data;

@Data
public class UserResponseDto {
    private Integer userId;
    private String email;
    private String nickname;
    private Boolean isActive; // true: 활성, false: 정지

    // 상태 라벨 변환
    public String getStatus() {
        return (isActive != null && isActive) ? "활성" : "정지";
    }
}
