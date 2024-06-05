package com.example.newspeed.dto;

import com.example.newspeed.entity.User;
import jakarta.persistence.Column;
import lombok.Getter;

@Getter
public class ProfileResponseDto {
    //사용자 ID, 이름, 한 줄 소개, 이메일을 볼 수 있습니다.
    private String userId;
    private String name;
    private String email;
    private String intro;

    public ProfileResponseDto(User user) {
        this.userId = user.getUserId();
        this.name = user.getUserName();
        this.email = user.getEmail();
        this.intro = user.getIntro();
    }

    public ProfileResponseDto(String userId, String name, String email, String intro) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.intro = intro;
    }

    public static ProfileResponseDto toDto(User user) {
        return new ProfileResponseDto(
                user.getUserId(),
                user.getUserName(),
                user.getEmail(),
                user.getIntro());
    }
}
