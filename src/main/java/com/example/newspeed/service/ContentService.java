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

    public Content getContentById(Long id) {
        return contentRepository.findById(id).orElseThrow(() -> new RuntimeException("content를 찾을 수 없습니다"));
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
        Content content = getContentById(id);
        if (!content.getAuthorId().equals(authorId)) {
            throw new RuntimeException("작성자가 아니여서 갱신할 수 없습니다.");
        }
        content.setContent(contents);
        content.setUpdatedDate(LocalDateTime.now());
        return contentRepository.save(content);
    }

    @Transactional
    public void deleteContent(Long id, Long authorId) {
        Content content = getContentById(id);
        if (!content.getAuthorId().equals(authorId)) {
            throw new RuntimeException("작성자가 아니여서 삭제할 수 없습니다.");
        }
        contentRepository.delete(content);
    }
}
