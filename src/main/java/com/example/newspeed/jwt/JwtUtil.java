package com.example.newspeed.jwt;


import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Base64;

@Component

public class JwtUtil {

    public static final String AUTHORIZATION_HEADER = "Authorization";
    // Token 식별자
    public static final String BEARER_PREFIX = "Bearer ";

    public static final String ACCESS_TOKEN_HEADER = "ACCESS_TOKEN_HEADER";
    public static final String REFRESH_TOKEN_HEADER = "REFRESH_TOKEN_HEADER";

    // Access Token 만료시간 설정 (10초)
    public final long ACCESS_TOKEN_EXPIRATION = 10 * 1000L; // 10초
    // Refresh Token 만료기간 설정(1시간)
    public final long REFRESH_TOKEN_EXPIRATION = 60 * 60 * 1000L; //1시간

    // 로그 설정
    public static final Logger logger = LoggerFactory.getLogger("JWT 관련 로그");

    @Value("${jwt.secret}")
    private String secret;
    private Key key;


    // 키 init 하는 메서드 구현해야함
    //    생성자가 만들어진후 한번만 실행됨
    @PostConstruct
    public void init() {
        byte[] bytes = Base64.getDecoder().decode(secret);
        key = Keys.hmacShaKeyFor(bytes);
    }

    }

