package com.example.newspeed.service;

import com.example.newspeed.dto.ContentDto;
import com.example.newspeed.entity.Content;
import com.example.newspeed.entity.User;
import com.example.newspeed.repository.ContentRepository;
import com.example.newspeed.security.UserDetailsImpl;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ContentService {
    @Autowired
    private ContentRepository contentRepository;

    public List<ContentDto> getAllContents() {
        return contentRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public Content getContentById2(Long id) {
        Content content = contentRepository.findById(id).orElseThrow(() -> new RuntimeException("content를 찾을 수 없습니다"));
        return content;
    }

    public ContentDto getContentById(Long id) {
        Content content = contentRepository.findById(id).orElseThrow(() -> new RuntimeException("content를 찾을 수 없습니다"));
        return convertToDto(content);
    }

    @Transactional
    public ContentDto createContent(UserDetailsImpl userDetails, String contents) {
        User user = userDetails.getUser();
        Content content = new Content();
        content.setUser(user);
        content.setContent(contents);
        content.setCreatedDate(LocalDateTime.now());
        Content savedContent = contentRepository.save(content);
        return convertToDto(savedContent);
    }

    @Transactional
    public ContentDto updateContent(Long id, UserDetailsImpl userDetails, String contents) {
        User user = userDetails.getUser();
        Content content = contentRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("content를 찾을 수 없습니다"));
        if (!content.getUser().getUserId().equals(user.getUserId())) {
            throw new IllegalArgumentException("작성자가 아니여서 갱신할 수 없습니다.");
        }
        content.setContent(contents);
        content.setUpdatedDate(LocalDateTime.now());
        Content updatedContent = contentRepository.save(content);
        return convertToDto(updatedContent);
    }

    @Transactional
    public void deleteContent(Long id, UserDetailsImpl userDetails) {
        User user = userDetails.getUser();
        Content content = contentRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("content를 찾을 수 없습니다"));
        if (!content.getUser().getUserId().equals(user.getUserId())) {
            throw new IllegalArgumentException("작성자가 아니여서 삭제할 수 없습니다.");
        }
        contentRepository.delete(content);
    }

    public Page<ContentDto> getContents(int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy).descending());
        Page<Content> contentPage = contentRepository.findAll(pageable);
        return contentPage.map(this::convertToDto);
    }

    public Page<ContentDto> getContentsSortedByCreatedAt(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        Page<Content> contentPage = contentRepository.findAll(pageable);
        return contentPage.map(this::convertToDto);
    }

    public Page<ContentDto> searchContentsByDateRange(LocalDateTime startDate, LocalDateTime endDate, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        Page<Content> contentPage = contentRepository.findByCreatedDateBetween(startDate, endDate, pageable);
        return contentPage.map(this::convertToDto);
    }

    public Page<ContentDto> getContentsOrderByLikes(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("likes").descending());
        Page<Content> contentPage = contentRepository.findAllByOrderByLikesDesc(pageable);
        return contentPage.map(this::convertToDto);
    }

    private ContentDto convertToDto(Content content) {
        return new ContentDto(content);
    }
}