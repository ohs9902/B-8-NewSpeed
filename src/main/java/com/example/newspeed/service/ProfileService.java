package com.example.newspeed.service;

import com.example.newspeed.dto.ProfileRequestDto;
import com.example.newspeed.dto.ProfileResponseDto;
import com.example.newspeed.entity.User;
import com.example.newspeed.repository.UserRepository;
import com.example.newspeed.security.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;

@Service
@Transactional
public class ProfileService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    @Autowired
    public ProfileService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public ProfileResponseDto getProfile(@PathVariable(name = "id") long id) {
        return ProfileResponseDto.toDto(findUser(id));
    }

    public ProfileResponseDto update(UserDetailsImpl userDetails, ProfileRequestDto requestDto) {
        User user = userDetails.getUser();

        user.update(requestDto.getName(), requestDto.getIntro());
        userRepository.save(user);
        return ProfileResponseDto.toDto(user);
    }

    public void updatePassword(UserDetailsImpl userDetails, ProfileRequestDto requestDto) {
        User user = userDetails.getUser();
        if (!passwordEncoder.matches(requestDto.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 맞지 않습니다.");
        }

        if(passwordEncoder.matches(requestDto.getNewPassword(), user.getPassword())){
            throw new IllegalArgumentException("이전과 같은 비밀번호입니다.");
        }


        user.updatePassword(passwordEncoder.encode(requestDto.getNewPassword()));
        userRepository.save(user);
    }

    protected User findUser(long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("프로필을 찾을 수 없습니다."));
    }
}
