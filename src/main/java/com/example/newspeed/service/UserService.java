package com.example.newspeed.service;

import com.example.newspeed.dto.SignUpRequestDto;
import com.example.newspeed.entity.User;
import com.example.newspeed.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;
    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
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




}
