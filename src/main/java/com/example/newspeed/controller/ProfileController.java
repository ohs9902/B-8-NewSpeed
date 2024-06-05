package com.example.newspeed.controller;

import com.example.newspeed.dto.ProfileRequestDto;
import com.example.newspeed.dto.ProfileResponseDto;
import com.example.newspeed.service.ProfileService;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<ProfileResponseDto> update(@RequestBody ProfileRequestDto requestDto) {
        return ResponseEntity.ok().body(profileService.update(requestDto));
    }

    @PutMapping("/password")
    public ResponseEntity<ProfileResponseDto> updatePassword(@RequestBody ProfileRequestDto requestDto) {
        return ResponseEntity.ok().body(profileService.updatePassword(requestDto));
    }
}