package com.jobdam.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VerificationRequestDto {

    @NotBlank
    @Email
    private String email;
}
