package com.example.newspeed.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class ProfileRequestDto {
    private long id;
    private String userId;
    private String password;
    private String name;
    private String email;
    private String intro;
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^*+=-])(?=.*[0-9]).{10,}$", message = "대소문자 포함 영문, 숫자, 특수문자를 포함하여 10자 이상 입력해주세요.")
    private String newPassword;
}
