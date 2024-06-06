package com.example.newspeed.service;

import com.example.newspeed.entity.Content;
import com.example.newspeed.entity.User;
import com.example.newspeed.jwt.JwtUtil;
import com.example.newspeed.repository.ContentRepository;
import com.example.newspeed.repository.UserRepository;
import com.example.newspeed.security.UserDetailsImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ContentService {
    private static final Logger log = LoggerFactory.getLogger(ContentService.class);
    @Autowired
    private ContentRepository contentRepository;
    @Autowired
    private JwtUtil jwtUtil;

    public List<Content> getAllContents() {
        return contentRepository.findAllByOrderByCreatedDateDesc();
    }

    public Content getContentById(Long id) {
        return contentRepository.findById(id).orElseThrow(() -> new RuntimeException("content를 찾을 수 없습니다"));
    }

    @Transactional
    public Content createContent(UserDetailsImpl userDetails, String contents) {

        User user = userDetails.getUser();
        log.info(user.getUserId());


        Content content = new Content();
        content.setContent(contents);
        content.setUser(user);
        content.setCreatedDate(LocalDateTime.now());
        return contentRepository.save(content);
    }

    @Transactional
    public Content updateContent(Long id, String contents) {
        Content content = getContentById(id);
        content.setContent(contents);
        content.setUpdatedDate(LocalDateTime.now());
        return contentRepository.save(content);
    }

    @Transactional
    public void deleteContent(Long id) {
        Content content = getContentById(id);
        contentRepository.delete(content);
    }

    /*@Transactional
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
    }*/
}
