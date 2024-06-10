package com.example.newspeed.controller;


import com.example.newspeed.dto.ContentDto;
import com.example.newspeed.dto.ContentRequestDto;
import com.example.newspeed.entity.Content;
import com.example.newspeed.security.UserDetailsImpl;
import com.example.newspeed.service.ContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/api/content")
public class ContentController {
    @Autowired
    private ContentService contentService;

    //전체조회
    @GetMapping
    public List<ContentDto> getAllNewsFeeds() {
        return contentService.getAllContents();
    }

    //특정조회
    @GetMapping("/{id}")
    public ContentDto getNewsFeedById(@PathVariable Long id) {
        return contentService.getContentById(id);
    }

    //생성
    @PostMapping
    public ContentDto createNewsFeed(@RequestBody ContentRequestDto request, @AuthenticationPrincipal UserDetailsImpl authentication) {
        return contentService.createContent(authentication, request.getContent());
    }

    //수정
    @PutMapping("/{id}")
    public ContentDto updateNewsFeed(@PathVariable Long id, @RequestBody ContentRequestDto request, @AuthenticationPrincipal UserDetailsImpl authentication) {
        return contentService.updateContent(id, authentication, request.getContent());
    }

    //삭제
    @DeleteMapping("/{id}")
    public void deleteNewsFeed(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl authentication) {
        contentService.deleteContent(id, authentication);
    }

    //페이지 기능
    @GetMapping("/pages")
    public Page<ContentDto> getContents(@RequestParam(defaultValue = "0") int page,
                                     @RequestParam(defaultValue = "10") int size,
                                     @RequestParam(defaultValue = "createdDate") String sortBy) {
        return contentService.getContents(page, size, sortBy);
    }

    //생성일자기준 정렬
    @GetMapping("/pages/sortedByCreatedAt")
    public Page<ContentDto> getContentsSortedByCreatedAt(@RequestParam(defaultValue = "0") int page,
                                                      @RequestParam(defaultValue = "10") int size) {
        return contentService.getContentsSortedByCreatedAt(page, size);
    }

    //특정기간 정렬
    @GetMapping("/pages/search")
    public Page<ContentDto> searchContents(@RequestParam String startDate,
                                        @RequestParam String endDate,
                                        @RequestParam(defaultValue = "0") int page,
                                        @RequestParam(defaultValue = "10") int size) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        LocalDateTime start = LocalDateTime.parse(startDate, formatter);
        LocalDateTime end = LocalDateTime.parse(endDate, formatter);
        return contentService.searchContentsByDateRange(start, end, page, size);
    }

    @GetMapping("/pages/sortedByLike")
    public Page<ContentDto> getContentsOrderByLikes(@RequestParam(defaultValue = "0") int page,
                                                 @RequestParam(defaultValue = "10") int size) {
        return contentService.getContentsOrderByLikes(page,size);

    }
}