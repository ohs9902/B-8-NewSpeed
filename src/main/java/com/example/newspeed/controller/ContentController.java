package com.example.newspeed.controller;


import com.example.newspeed.dto.ContentRequestDto;
import com.example.newspeed.entity.Content;
import com.example.newspeed.service.ContentService;
import org.springframework.beans.factory.annotation.Autowired;
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
        return contentService.getNewsFeedById(id);
    }

    @PostMapping
    public Content createNewsFeed(@RequestBody ContentRequestDto request, Authentication authentication) {
        Long authorId = Long.parseLong(authentication.getName());
        return contentService.createContent(authorId, request.getContent());
    }

    @PutMapping("/{id}")
    public Content updateNewsFeed(@PathVariable Long id, @RequestBody ContentRequestDto request, Authentication authentication) {
        Long authorId = Long.parseLong(authentication.getName());
        return contentService.updateContent(id, authorId, request.getContent());
    }

    @DeleteMapping("/{id}")
    public void deleteNewsFeed(@PathVariable Long id, Authentication authentication) {
        Long authorId = Long.parseLong(authentication.getName());
        contentService.deleteContent(id, authorId);
    }
}
