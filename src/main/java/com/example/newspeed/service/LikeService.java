package com.example.newspeed.service;

import com.example.newspeed.entity.Content;
import com.example.newspeed.entity.Like;
import com.example.newspeed.entity.User;
import com.example.newspeed.repository.ContentRepository;
import com.example.newspeed.repository.LikeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class LikeService {
    @Autowired
    ContentRepository contentRepository;
    @Autowired
    private LikeRepository likeRepository;


    public ResponseEntity<String> contentLike(Long contentId, User user) {

        // 게시물 존재 체크
        Content content = contentRepository.findById(contentId).orElseThrow(() ->
                new IllegalArgumentException("선택한 개시물이 없습니다.")
        );

        //자신의 게시물인지 제크
        if (content.getUser().getUserId().equals(user.getUserId())) {
            throw new IllegalArgumentException("자신의 게시물에는 좋아요를 누를 수 없습니다.");
        }
        // 좋아요가 중복인지 체크


        //좋아요 츄가
        Like like = new Like();
        like.setUser(user);
        like.setContent(content);
        like.setCreatedAt(LocalDateTime.now());
        likeRepository.save(like);

        return ResponseEntity.ok("좋아요 성공.");
    }

    public ResponseEntity<String> contentUnlike(Long contentId, User user) {

        // // 게시물 존재 체크
        Content content = contentRepository.findById(contentId).orElseThrow(() ->
                new IllegalArgumentException("선택한 개시물이 없습니다.")
        );



        return null;
    }
}
