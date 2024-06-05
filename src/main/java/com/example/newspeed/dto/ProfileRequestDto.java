package com.example.newspeed.dto;

import jakarta.persistence.Column;
import lombok.Getter;

@Getter
public class ProfileRequestDto {
    private long id;
    private String userId;
    private String password;
    private String name;
    private String email;
    private String intro;
    private String newPassword;
}
