package com.example.newspeed.service;

import com.example.newspeed.dto.LoginRequestDto;
import com.example.newspeed.dto.SignUpRequestDto;
import com.example.newspeed.entity.User;
import com.example.newspeed.jwt.JwtUtil;
import com.example.newspeed.jwt.LogoutFilter;
import com.example.newspeed.repository.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final LogoutFilter logoutFilter;
    private final PasswordEncoder passwordEncoder;
    @Autowired
    public UserService(UserRepository userRepository, LogoutFilter logoutFilter, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.logoutFilter = logoutFilter;
        this.passwordEncoder = passwordEncoder;
    }

    public void singUp(SignUpRequestDto signUpRequestDto){
        String userId = signUpRequestDto.getUserId();
        String password = passwordEncoder.encode(signUpRequestDto.getPassword());
        String name = signUpRequestDto.getUsername();
        String email = signUpRequestDto.getEmail();
        String intro = signUpRequestDto.getIntro();
        String status = signUpRequestDto.getStatus();
        Optional<User> checkUserId = userRepository.findByUserId(userId);
        Optional<User> checkEmail = userRepository.findByEmail(email);
        if(checkUserId.isPresent()){
            throw new IllegalArgumentException("이미 존재하는 id 입니다.");
        }
        if(checkEmail.isPresent()){
            throw new IllegalArgumentException("이미 존재하는 email 입니다.");
        }

        User user = new User(userId,password,name,email,intro,status);
        userRepository.save(user);
    }
    @Transactional
    public void withdrawal(LoginRequestDto loginRequestDto, HttpServletResponse res) throws ServletException, IOException { //회원삭제에 필요한 필드와 로그인에 필요한 필드가 동일함으로 dto재사용
        System.out.println("회원탈퇴 서비스 진입");
        String password = passwordEncoder.encode(loginRequestDto.getPassword());
       Optional<User> optionalUser = userRepository.findByUserId(loginRequestDto.getUserId());
       if (optionalUser.isPresent()){
           User user = optionalUser.get();
           if(password.equals(password)){
               user.setStatus("탈퇴");
               userRepository.save(user);
               SecurityContextHolder.clearContext(); // 현재 사용자의 인증 정보를 제거
               JwtUtil jwtUtil = new JwtUtil();
               jwtUtil.clearCookies(res);

           }else{
               throw new BadCredentialsException("비밀번호가 일치하지 않습니다");
           }
       }else{
           throw new UsernameNotFoundException("사용자를 찾을 수 없거나 비밀번호가 일치하지 않습니다.");
       }


    }




}
