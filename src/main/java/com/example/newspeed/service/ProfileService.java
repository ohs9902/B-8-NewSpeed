package com.example.newspeed.service;

import com.example.newspeed.dto.ProfileRequestDto;
import com.example.newspeed.dto.ProfileResponseDto;
import com.example.newspeed.entity.User;
import com.example.newspeed.repository.ProfileRepository;
import com.example.newspeed.repository.UserRepository;
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

    public ProfileResponseDto update(ProfileRequestDto requestDto) {
        User user = findUser(requestDto.getId());

        user.update(requestDto.getName(), requestDto.getEmail(), requestDto.getIntro());
        return ProfileResponseDto.toDto(user);
    }

    public ProfileResponseDto updatePassword(ProfileRequestDto requestDto) {
        User user = findIDPwd(requestDto.getId(), requestDto.getPassword());

        if(user.getPassword().equals(requestDto.getNewPassword())){
            throw new IllegalArgumentException("이전과 같은 비밀번호입니다.");
        } // 비밀번호 형식 예외처리 구현

        user.updatePassword(requestDto.getNewPassword());

        return ProfileResponseDto.toDto(user);
    }

    protected User findUser(long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("프로필을 찾을 수 없습니다."));
    }

    protected User findIDPwd(long id, String password) {
        User user = findUser(id);
        if (!user.getPassword().equals(password)) {
            throw new IllegalArgumentException("비밀번호가 맞지 않습니다.");
        }
        return user;
    }
}
