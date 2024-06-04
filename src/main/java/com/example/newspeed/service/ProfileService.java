package com.example.newspeed.service;

import com.example.newspeed.dto.ProfileResponseDto;
import com.example.newspeed.entity.User;
import com.example.newspeed.repository.ProfileRepository;
import com.example.newspeed.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Service
public class ProfileService {
    private final UserRepository userRepository;

    public ProfileService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public ProfileResponseDto getProfile(@PathVariable(name = "id") long id) {
        return ProfileResponseDto.toDto(findUser(id));
    }

    protected User findUser(long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("프로필을 찾을 수 없습니다."));
    }
}
