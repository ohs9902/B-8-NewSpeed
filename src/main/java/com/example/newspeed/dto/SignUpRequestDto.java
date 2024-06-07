package com.example.newspeed.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignUpRequestDto {
    @NotNull
    @Size(min = 10, max = 20, message = "10에서20자 이내로 만 가능합니다.")
    @Pattern(regexp = "^[a-zA-Z0-9]*$", message = "대소문자 영어와 숫자만 가능합니다.")
    private String userId;

    @NotNull
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^*+=-])(?=.*[0-9]).{10,}$", message = "대소문자 포함 영문, 숫자, 특수문자를 포함하여 10자 이상 입력해주세요.")
    private String password;

    private String Username;

    @NotNull
    @Email(message = "이메일 형식이 아닙니다.")
    private String email;

    @Size(max = 255, message = "255자 이내로만 작성 가능합니다.")
    private String intro;
    private String status;
}
