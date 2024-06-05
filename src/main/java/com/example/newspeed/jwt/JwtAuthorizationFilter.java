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

        String accessToken = jwtUtil.getJwtFromHeader(req, JwtUtil.ACCESS_TOKEN_HEADER);

        if (StringUtils.hasText(accessToken)) {
            // access토큰 감지
            if (jwtUtil.validateToken(accessToken)) {
                Claims info = jwtUtil.getUserInfoFromToken(accessToken);
                try {
                    setAuthentication(info.getSubject());
                } catch (Exception e) {
                    log.error(e.getMessage());
                    return;
                }
            } else {
                // refresh토큰 감지
                String refreshToken = jwtUtil.getJwtFromHeader(req, JwtUtil.REFRESH_TOKEN_HEADER);
                if (StringUtils.hasText(refreshToken) && jwtUtil.validateToken(refreshToken)) {
                    Claims refreshTokenClaims = jwtUtil.getUserInfoFromToken(refreshToken);
                    String username = refreshTokenClaims.getSubject();
                    UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                    // 새로운 access토큰
                    String newAccessToken = jwtUtil.generateToken(userDetails.getUsername(), 10 * 1000L,JwtUtil.ACCESS_TOKEN_HEADER);

                    // 새로운 access토큰
                    res.setHeader(JwtUtil.ACCESS_TOKEN_HEADER, newAccessToken);

                    setAuthentication(username);
                } else {
                    log.error("Both Access and Refresh tokens are invalid");
                    return;
                }
            }
        }
        filterChain.doFilter(req, res);
    }

    // 인증 처리
    public void setAuthentication(String username) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = createAuthentication(username);
        context.setAuthentication(authentication);

        SecurityContextHolder.setContext(context);
    }

    // 인증 객체 생성
    private Authentication createAuthentication(String username) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }
}