package com.example.newspeed.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class User extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String userId;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String userName;

    @Column(nullable = false)
    private String email;

    private String intro;

    @Column(nullable = false)
    private String status;

    private String refreshToken;

    @OneToMany
    private List<Content>contentList = new ArrayList<>();

    public User(String userId, String password, String name, String email, String intro, String status) {
        this.userId = userId;
        this.password = password;
        this.userName = name;
        this.email = email;
        this.intro = intro;
        this.status = status;
    }
}
