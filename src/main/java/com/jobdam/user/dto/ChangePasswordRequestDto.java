package com.jobdam.user.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangePasswordRequestDto {
    private String currentPassword;
    private String newPassword;
}
