package com.hanghae.eunda.dto.member;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignupRequestDto {
    @NotEmpty(message = "이메일 입력은 필수 입니다.")
    @Email(message = "올바른 이메일 형식이 아닙니다.")
    private String email;
    private String nickname;
    @Size(min = 8, max = 15, message = "비밀번호는 최소 8자 이상 최대 15자 이하이어야 합니다.")
    private String password;
    private String password2;

    public boolean isPasswordConfirmed() {
        return password != null && password.equals(password2);
    }
}
