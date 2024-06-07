package com.example.newspeed.jwt;

import com.example.newspeed.security.UserDetailsServiceImpl;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j(topic = "JWT 검증 및 인가")
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;
    private static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    public JwtAuthorizationFilter(JwtUtil jwtUtil, UserDetailsServiceImpl userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain filterChain) throws ServletException, IOException {
        String path = req.getRequestURI();
        if (PATH_MATCHER.match("/swagger-ui/**", path) || PATH_MATCHER.match("/v3/api-docs/**", path)|| PATH_MATCHER.match("/api/user/**", path)) {
            filterChain.doFilter(req, res);
            return;
        }
        //AccessToken 가져온후 가공
        String accessToken = jwtUtil.getAccessTokenFromRequest(req);

        //토큰 만료만 검사하기 위한 메서드
        boolean accessTokenExpiration = checkAccessToken(accessToken);


        // Refresh토큰 감지(가져오기),가공하기
        String refreshToken = jwtUtil.getRefreshTokenFromRequest(req);
        refreshToken = jwtUtil.substringToken(refreshToken);

        //NullCheck
        if(!StringUtils.hasText(refreshToken)){
            return;
        }

        //리프레쉬 토큰 유효성 검사 후 새 Access 토큰 발급
        if (jwtUtil.validateToken(refreshToken) && accessTokenExpiration) {
            Claims refreshTokenClaims = jwtUtil.getUserInfoFromToken(refreshToken);
            String username = refreshTokenClaims.getSubject();
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            // 새로운 access토큰
            String newAccessToken = jwtUtil.generateToken(userDetails.getUsername(), jwtUtil.ACCESS_TOKEN_EXPIRATION, JwtUtil.ACCESS_TOKEN_HEADER);
            jwtUtil.addJwtToCookie(res, newAccessToken, JwtUtil.ACCESS_TOKEN_HEADER);
            log.info("새 Access Token 발급 ");
            log.info("새 Access Token : " + newAccessToken);

            try {
                setAuthentication(refreshTokenClaims.getSubject());
            } catch (Exception e) {
                log.error(e.getMessage());
                return;
            }
        } else {
            log.error("Both Access and Refresh tokens are invalid");
            return;
        }

        filterChain.doFilter(req, res);
    }

    private boolean checkAccessToken(String accessToken) {
        // hasText : Null체크
        if (!StringUtils.hasText(accessToken)) {
            log.error("AccessToken이 없습니다.");
            return false;
        }
        // JWT 토큰 substring
        accessToken = jwtUtil.substringToken(accessToken);
        log.info("AccessToken= " + accessToken);

        // Access 토큰 유효성 검사
        try {
            return jwtUtil.validateToken(accessToken);
        } catch (Exception e) {
            log.error("Access Token Error");
        }
        return false;
    }

    // 인증 처리
    public void setAuthentication(String userId) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = createAuthentication(userId);
        context.setAuthentication(authentication);

        SecurityContextHolder.setContext(context);
    }

    // 인증 객체 생성
    private Authentication createAuthentication(String userId) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(userId);
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }
}