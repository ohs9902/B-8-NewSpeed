package com.example.newspeed.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class User extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String userId;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    private String intro;

    @Column(nullable = false)
    private String status;

    private String refreshToken;

    @JoinColumn(name = "")
    @OneToMany
    private List<Content>contentList = new ArrayList<>();

}
