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
        if (PATH_MATCHER.match("/swagger-ui/**", path) || PATH_MATCHER.match("/v3/api-docs/**", path) || PATH_MATCHER.match("/api/user/**", path)) {
            filterChain.doFilter(req, res);
            return;
        }
        //AccessToken 가져온후 가공
        String accessToken = jwtUtil.getAccessTokenFromRequest(req);
        // JWT 토큰 substring
        accessToken = jwtUtil.substringToken(accessToken);

        //검사
        checkAccessToken(accessToken);
        log.info("AccessToken= " + accessToken);

        Claims accessTokenClaims = jwtUtil.getUserInfoFromToken(accessToken);

        // 인증처리
        try {
            setAuthentication(accessTokenClaims.getSubject());
        } catch (Exception e) {
            log.error(e.getMessage());
            return;
        }
        filterChain.doFilter(req, res);
    }

    private void checkAccessToken(String accessToken) {
        // hasText : Null체크
        if (!StringUtils.hasText(accessToken)) {
            log.error("AccessToken이 없습니다.");
            return;
        }
        // Access 토큰 유효성 검사
        try {
            jwtUtil.validateToken(accessToken);
        } catch (Exception e) {
            log.error("Access Token Error");
        }
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