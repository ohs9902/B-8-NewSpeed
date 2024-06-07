package com.example.newspeed.service;

import com.example.newspeed.dto.ProfileRequestDto;
import com.example.newspeed.dto.ProfileResponseDto;
import com.example.newspeed.entity.User;
import com.example.newspeed.repository.ProfileRepository;
import com.example.newspeed.repository.UserRepository;
import com.example.newspeed.security.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Service
@Transactional
public class ProfileService {
    private final UserRepository userRepository;
    @Autowired
    public ProfileService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public ProfileResponseDto getProfile(@PathVariable(name = "id") long id) {
        return ProfileResponseDto.toDto(findUser(id));
    }

    public ProfileResponseDto update(UserDetailsImpl userDetails, ProfileRequestDto requestDto) {
        User user = userDetails.getUser();

        user.update(requestDto.getName(), requestDto.getIntro());
        return ProfileResponseDto.toDto(user);
    }

    public ProfileResponseDto updatePassword(UserDetailsImpl userDetails, ProfileRequestDto requestDto) {
        User user = userDetails.getUser();
        if (!user.getPassword().equals(requestDto.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 맞지 않습니다.");
        }

        if(user.getPassword().equals(requestDto.getNewPassword())){
            throw new IllegalArgumentException("이전과 같은 비밀번호입니다.");
        }


        user.updatePassword(requestDto.getNewPassword());

        return ProfileResponseDto.toDto(user);
    }

    protected User findUser(long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("프로필을 찾을 수 없습니다."));
    }
}
