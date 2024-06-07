package com.example.newspeed.controller;

import com.example.newspeed.entity.Content;
import com.example.newspeed.entity.User;
import com.example.newspeed.security.UserDetailsImpl;
import com.example.newspeed.service.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class LikeController {
    @Autowired
    LikeService likeService;

    //게시물 좋아요
    @PostMapping("/like/content/{contentId}")
    public ResponseEntity<String> like(@PathVariable Long contentId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        User user = userDetails.getUser();
       return likeService.contentLike(contentId,user);

    }

    @DeleteMapping("/unlike/content/{contentId}")
    public ResponseEntity<String> unlike(@PathVariable Long contentId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        User user = userDetails.getUser();
        return likeService.contentUnlike(contentId,user);
    }



}
