package com.example.newspeed.service;

import com.example.newspeed.entity.Content;
import com.example.newspeed.repository.ContentRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ContentService {
    @Autowired
    private ContentRepository contentRepository;

    public List<Content> getAllContents() {
        return contentRepository.findAllByOrderByCreatedDateDesc();
    }

    public Content getNewsFeedById(Long id) {
        return contentRepository.findById(id).orElseThrow(() -> new RuntimeException("NewsFeed not found"));
    }

    @Transactional
    public Content createContent(Long authorId, String contents) {
        Content content = new Content();
        content.setAuthorId(authorId);
        content.setContent(contents);
        content.setCreatedDate(LocalDateTime.now());
        return contentRepository.save(content);
    }

    @Transactional
    public Content updateContent(Long id, Long authorId, String contents) {
        Content content = getNewsFeedById(id);
        if (!content.getAuthorId().equals(authorId)) {
            throw new RuntimeException("You are not authorized to update this post");
        }
        content.setContent(contents);
        content.setUpdatedDate(LocalDateTime.now());
        return contentRepository.save(content);
    }

    @Transactional
    public void deleteContent(Long id, Long authorId) {
        Content content = getNewsFeedById(id);
        if (!content.getAuthorId().equals(authorId)) {
            throw new RuntimeException("You are not authorized to delete this post");
        }
        contentRepository.delete(content);
    }
}
