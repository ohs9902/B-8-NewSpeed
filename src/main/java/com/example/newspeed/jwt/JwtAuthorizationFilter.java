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
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j(topic = "JWT 검증 및 인가")
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;

    public JwtAuthorizationFilter(JwtUtil jwtUtil, UserDetailsServiceImpl userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain filterChain) throws ServletException, IOException {

        //AccessToken 가져온후 가공
        String accessToken = jwtUtil.getAccessTokenFromRequest(req);
        accessToken = jwtUtil.substringToken(accessToken);


        //NullCheck
        if (!StringUtils.hasText(accessToken)) {
            return;
        }

        // access토큰 감지(검증)
        // 수정해야할 점 : AccessToken이 단순 만료 일때는 오류 메세지만 나오고, 필터는 종료되면 안됨
        if (jwtUtil.validateToken(accessToken)) {
            Claims info = jwtUtil.getUserInfoFromToken(accessToken);
            try {
                setAuthentication(info.getSubject());
            } catch (Exception e) {
                log.error(e.getMessage());
                return;
            }
        }

        // Refresh토큰 감지(가져오기),가공하기
        String refreshToken = jwtUtil.getRefreshTokenFromRequest(req);
        refreshToken = jwtUtil.substringToken(refreshToken);

        //리프레쉬 토큰 유효성 검사 후 새 Access 토큰 발급
        if (jwtUtil.validateToken(refreshToken)) {
            Claims refreshTokenClaims = jwtUtil.getUserInfoFromToken(refreshToken);
            String username = refreshTokenClaims.getSubject();
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            // 새로운 access토큰
            String newAccessToken = jwtUtil.generateToken(userDetails.getUsername(), jwtUtil.ACCESS_TOKEN_EXPIRATION, JwtUtil.ACCESS_TOKEN_HEADER);
            jwtUtil.addJwtToCookie(res, newAccessToken, JwtUtil.ACCESS_TOKEN_HEADER);

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