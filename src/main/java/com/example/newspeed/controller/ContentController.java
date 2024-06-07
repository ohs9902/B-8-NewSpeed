package com.example.newspeed.controller;


import com.example.newspeed.dto.ContentRequestDto;
import com.example.newspeed.entity.Content;
import com.example.newspeed.security.UserDetailsImpl;
import com.example.newspeed.service.ContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/content")
public class ContentController {
    @Autowired
    private ContentService contentService;

    @GetMapping
    public List<Content> getAllNewsFeeds() {
        return contentService.getAllContents();
    }

    @GetMapping("/{id}")
    public Content getNewsFeedById(@PathVariable Long id) {
        return contentService.getContentById(id);
    }

    @PostMapping
    public Content createNewsFeed(@RequestBody ContentRequestDto request, @AuthenticationPrincipal UserDetailsImpl authentication) {
        Long authorId = Long.parseLong(authentication.getUsername());
        return contentService.createContent(authorId, request.getContent());
    }

    @PutMapping("/{id}")
    public Content updateNewsFeed(@PathVariable Long id, @RequestBody ContentRequestDto request, @AuthenticationPrincipal UserDetailsImpl authentication) {
        Long authorId = Long.parseLong(authentication.getUsername());
        return contentService.updateContent(id, authorId, request.getContent());
    }

    @DeleteMapping("/{id}")
    public void deleteNewsFeed(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl authentication) {
        Long authorId = Long.parseLong(authentication.getUsername());
        contentService.deleteContent(id, authorId);
    }
}
