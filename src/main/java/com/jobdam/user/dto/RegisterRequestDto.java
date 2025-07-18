package com.jobdam.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequestDto {

    /*
        논의 후 변경해도 괜찮은 사항들입니다.
        사이즈 제약(현재 적용값 : 닉네임 2~20자 / 비밀번호 8자 이상)
        제약에 따른 모든 메세지
    */

    @NotBlank(message="사용자 닉네임은 필수 입력값입니다.")
    @Size(min=2, max=20, message="사용자 닉네임은 2자리 이상, 20자리 이하여야 합니다.")
    private String nickname;

    @NotBlank(message="사용자 비밀번호는 필수 입력값입니다.")
    @Size(min=8, message="사용자 비밀번호는 8자리 이상이어야 합니다.")
    private String password;

    @NotBlank(message="사용자 이메일은 필수 입력값입니다.")
    @Email(message="올바른 이메일 형식이 아닙니다.")
    private String email;

    private String phone;

    private String profileImageUrl;


}
