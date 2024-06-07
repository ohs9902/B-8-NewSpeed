package com.example.newspeed.service;

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
import org.springframework.security.core.userdetails.UserDetails;
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
    public Content createContent(UserDetailsImpl userDetails, String contents) {
        User user = userDetails.getUser();
        Content content = new Content();
        content.setUser(user);
        content.setContent(contents);
        content.setCreatedDate(LocalDateTime.now());
        return contentRepository.save(content);
    }

    @Transactional
    public Content updateContent(Long id, UserDetailsImpl userDetails, String contents) {
        User user = userDetails.getUser();
        Content content = getContentById(id);
        if (!content.getUser().equals(user)) {
            throw new RuntimeException("작성자가 아니여서 갱신할 수 없습니다.");
        }
        content.setContent(contents);
        content.setUpdatedDate(LocalDateTime.now());
        return contentRepository.save(content);
    }

    @Transactional
    public void deleteContent(Long id, UserDetailsImpl userDetails) {
        User user = userDetails.getUser();
        Content content = getContentById(id);
        if (!content.getUser().equals(user)) {
            throw new RuntimeException("작성자가 아니여서 삭제할 수 없습니다.");
        }
        contentRepository.delete(content);
    }

    //페이지 정렬
    public Page<Content> getContents(int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy).descending());
        return contentRepository.findAll(pageable);
    }


    //생성일자기준 정렬
    public Page<Content> getContentsSortedByCreatedAt(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        return contentRepository.findAll(pageable);
    }

    //특정기간 정렬
    public Page<Content> searchContentsByDateRange(LocalDateTime startDate, LocalDateTime endDate, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        return contentRepository.findByCreatedDateBetween(startDate, endDate, pageable);
    }
}