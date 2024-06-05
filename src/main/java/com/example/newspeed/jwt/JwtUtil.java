package com.example.newspeed.jwt;


import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Base64;
import org.springframework.util.StringUtils;

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
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    // 키 init 하는 메서드 구현해야함

    //    생성자가 만들어진후 한번만 실행됨

    @PostConstruct
    public void init() {
        byte[] bytes = Base64.getDecoder().decode(secret);
        key = Keys.hmacShaKeyFor(bytes);
    }

    //토큰 생성 Access, Refresh
    public String generateToken(String username, Long expires, String tokenType) {
        Date date = new Date();

        return BEARER_PREFIX +
            Jwts.builder().setSubject(username)
                .claim(AUTHORIZATION_HEADER, tokenType)
                .setExpiration(new Date(date.getTime() + expires))
                .setIssuedAt(date)
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    //사용자에게서 토큰을 가져오기
    public String getAccessTokenFromRequest(HttpServletRequest req) {
        return getTokenFromRequest(req, ACCESS_TOKEN_HEADER);
    }

    public String getRefreshTokenFromRequest(HttpServletRequest req) {
        return getTokenFromRequest(req, REFRESH_TOKEN_HEADER);
    }

    //HttpServletRequest 에서 Cookie Value  JWT 가져오기
    public String getTokenFromRequest(HttpServletRequest req, String headerName){
        Cookie[] cookies = req.getCookies();
        if(cookies != null){
            for(Cookie cookie : cookies){
                if(cookie.getName().equals(headerName)){
                    try {
                        return URLDecoder.decode(cookie.getValue(), "UTF-8");
                    }catch (UnsupportedEncodingException e) {
                        return null;
                    }
                }
            }
        }
        return null;
    }
}

