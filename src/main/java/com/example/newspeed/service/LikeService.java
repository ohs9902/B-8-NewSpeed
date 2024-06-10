package com.example.newspeed.service;

import com.example.newspeed.entity.Comment;
import com.example.newspeed.entity.Content;
import com.example.newspeed.entity.Like;
import com.example.newspeed.entity.User;
import com.example.newspeed.repository.CommentRepository;
import com.example.newspeed.repository.ContentRepository;
import com.example.newspeed.repository.LikeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class LikeService {
    @Autowired
    ContentRepository contentRepository;
    @Autowired
    private LikeRepository likeRepository;
    @Autowired
    private CommentRepository commentRepository;


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
        Optional<Like> existingLike = likeRepository.findByUserAndContent(user, content);
        if (existingLike.isPresent()) {
            throw new IllegalArgumentException("이미 좋아요를 누른 게시글 입니다.");
        }
        //좋아요 츄가
        Like like = new Like();
        like.setUser(user);
        like.setContent(content);
        like.setCreatedAt(LocalDateTime.now());

        //content에 있는 like 리스트 추가
        content.addLike(like);
        // likeEntity 저장
        likeRepository.save(like);

        return ResponseEntity.ok("좋아요 성공.");
    }

    public ResponseEntity<String> contentUnlike(Long contentId, User user) {

        // // 게시물 존재 체크
        Content content = contentRepository.findById(contentId).orElseThrow(() ->
                new IllegalArgumentException("선택한 개시물이 없습니다.")
        );

        Like like = likeRepository.findByUserAndContent(user,content).orElseThrow(()->
                new IllegalArgumentException("이 게시물에 좋아요를 한 적이 없습니다.")
                );
        content.removeLike(like);
        likeRepository.delete(like);

        return ResponseEntity.ok("좋아요 취소 완료.");
    }

    public ResponseEntity<String> commentLike(Long commentId, User user) {
        // 게시물 존재 체크
        Comment comment = commentRepository.findById(commentId).orElseThrow(() ->
                new IllegalArgumentException("선택한 댓글이 없습니다.")
        );

        //자신의 게시물인지 제크
        if (comment.getUser().getUserId().equals(user.getUserId())) {
            throw new IllegalArgumentException("자신의 댓글에는 좋아요를 누를 수 없습니다.");
        }
        // 좋아요가 중복인지 체크
        Optional<Like> existingLike = likeRepository.findByUserAndComment(user, comment);
        if (existingLike.isPresent()) {
            throw new IllegalArgumentException("이미 좋아요를 누른 댓글 입니다.");
        }
        //좋아요 츄가
        Like like = new Like();
        like.setUser(user);
        like.setComment(comment);
        like.setCreatedAt(LocalDateTime.now());

        //content에 있는 like 리스트 추가
        comment.addLike(like);
        // likeEntity 저장
        likeRepository.save(like);

        return ResponseEntity.ok("좋아요 성공.");
    }


    //댓글 좋아요 취소
    public ResponseEntity<String> commentUnlike(Long commentId, User user) {

        // // 게시물 존재 체크
        Comment comment = commentRepository.findById(commentId).orElseThrow(() ->
                new IllegalArgumentException("선택한 댓글이 없습니다.")
        );

        Like like = likeRepository.findByUserAndComment(user,comment).orElseThrow(()->
                new IllegalArgumentException("이 댓글에 좋아요를 한 적이 없습니다.")
        );
        likeRepository.delete(like);

        return ResponseEntity.ok(" 좋아요 취소 완료.");
    }
}
