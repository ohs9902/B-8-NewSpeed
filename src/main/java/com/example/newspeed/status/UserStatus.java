package com.example.newspeed.status;

public enum UserStatus {
    ACTIVE("정상"),
    WITHDRAWN("탈퇴");
    private final String description;

    UserStatus(String description) {
        this.description = description;
    }
}
