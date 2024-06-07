package com.example.newspeed.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.codec.Utf8;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
@Slf4j(topic = "로그아웃")
public class LogoutFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;

    public LogoutFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if("/api/user/logout".equals(request.getRequestURI()) &&"POST".equalsIgnoreCase(request.getMethod())){
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if(auth!= null){
                new SecurityContextLogoutHandler().logout(request,response,auth);
            }
            jwtUtil.clearCookies(response);
            response.setCharacterEncoding("UTF-8");
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write("로그아웃 성공");
            log.info("로그아웃 성공");
            return;
        }
        filterChain.doFilter(request,response);
    }
}
