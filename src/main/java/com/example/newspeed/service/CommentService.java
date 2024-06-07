package com.example.newspeed.service;

import com.example.newspeed.dto.CommentRequest;
import com.example.newspeed.dto.CommentGetResponse;
import com.example.newspeed.dto.ContentDto;
import com.example.newspeed.entity.Comment;
import com.example.newspeed.entity.Content;
import com.example.newspeed.entity.User;
import com.example.newspeed.repository.CommentRepository;
import com.example.newspeed.security.UserDetailsImpl;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final ContentService contentService;


    //아이디로 Comment 찾기
    public Comment findById(Long id) {
        return commentRepository.findById(id).orElseThrow(() -> new RuntimeException("Comment not found"));
    }

    // 자신의 댓글인지 확인
    public void checkUser(Long commentId, UserDetailsImpl userDetails) {
        Comment comment = findById(commentId);
        Long commentUserId = comment.getUser().getId();

        User user = userDetails.getUser();
        Long userId = user.getId();

        if (!comment.getUser().getId().equals(userId)) {
            throw new RuntimeException("Comment user id mismatch");
        }

    }

    //댓글 생성
    @Transactional
    public Long create(Long contentId, CommentRequest request, UserDetailsImpl userDetails) {
        User user = userDetails.getUser();
        Content content = contentService.getContentById2(contentId);
        Comment comment = new Comment(user, request.getComment(), content);
        commentRepository.save(comment);
        return comment.getId();
    }

    //댓글 조회
    @Transactional
    public CommentGetResponse get(Long commentId) {
        Comment comment = findById(commentId);
        return new CommentGetResponse(comment.getId(), comment.getUser().getId(), comment.getComment());
    }

    //댓글 수정
    @Transactional
    public Long update(Long commentId, CommentRequest request, UserDetailsImpl userDetails) {
        checkUser(commentId, userDetails);
        Comment comment = findById(commentId);
        comment.setComment(request.getComment());
        commentRepository.save(comment);
        return comment.getId();
    }

    //댓글 삭제
    @Transactional
    public Long delete(Long commentId, UserDetailsImpl userDetails) {
        checkUser(commentId, userDetails);
        Comment comment = findById(commentId);
        commentRepository.delete(comment);
        return comment.getId();
    }
}
