package com.example.newspeed.controller;

import com.example.newspeed.dto.ProfileRequestDto;
import com.example.newspeed.dto.ProfileResponseDto;
import com.example.newspeed.security.UserDetailsImpl;
import com.example.newspeed.service.ProfileService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/profile")
public class ProfileController {
    private final ProfileService profileService;

    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProfileResponseDto> getProfile(@PathVariable(name = "id") long id) {
        return ResponseEntity.ok().body(profileService.getProfile(id));
    }

    @PutMapping
    public ResponseEntity<ProfileResponseDto> update(@RequestBody ProfileRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl authentication) {
        return ResponseEntity.ok().body(profileService.update(authentication, requestDto));
    }

    @PutMapping("/password")
    public ResponseEntity<ProfileResponseDto> updatePassword(@AuthenticationPrincipal UserDetailsImpl authentication, @Valid @RequestBody ProfileRequestDto requestDto) {
        return ResponseEntity.ok().body(profileService.updatePassword(authentication, requestDto));
    }
}