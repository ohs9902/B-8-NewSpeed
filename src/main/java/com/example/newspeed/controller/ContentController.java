package com.example.newspeed.controller;


import com.example.newspeed.dto.ContentRequestDto;
import com.example.newspeed.entity.Content;
import com.example.newspeed.entity.User;
import com.example.newspeed.repository.UserRepository;
import com.example.newspeed.security.UserDetailsImpl;
import com.example.newspeed.service.ContentService;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/content")
public class ContentController {
    private static final Logger log = LoggerFactory.getLogger(ContentController.class);
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
    // 필터 부분에서 작동하는 UserDetailsImpl 을 가져와서 로그인 되어 있는 유저 가져오기
    public Content createNewsFeed(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody ContentRequestDto request) {

        return contentService.createContent(userDetails ,request.getContent());
    }

    @PutMapping("/{id}")
    public Content updateNewsFeed(@PathVariable Long id, @RequestBody ContentRequestDto request) {
        return contentService.updateContent(id, request.getContent());
    }

    @DeleteMapping("/{id}")
    public void deleteNewsFeed(@PathVariable Long id) {
        contentService.deleteContent(id);
    }

/*    @PostMapping
    public Content createNewsFeed(@RequestBody contentRequestDto request, Authentication authentication) {
        Long authorId = Long.parseLong(authentication.getName());
        return contentService.createContent(authorId, request.getContent());
    }

    @PutMapping("/{id}")
    public Content updateNewsFeed(@PathVariable Long id, @RequestBody contentRequestDto request, Authentication authentication) {
        Long authorId = Long.parseLong(authentication.getName());
        return contentService.updateContent(id, authorId, request.getContent());
    }

    @DeleteMapping("/{id}")
    public void deleteNewsFeed(@PathVariable Long id, Authentication authentication) {
        Long authorId = Long.parseLong(authentication.getName());
        contentService.deleteContent(id, authorId);
    }*/
}
