package com.example.newspeed.entity;

import jakarta.persistence.*;

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
}
