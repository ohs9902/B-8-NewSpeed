package com.example.newspeed.controller;


import com.example.newspeed.dto.CommentRequest;
import com.example.newspeed.dto.CommentGetResponse;
import com.example.newspeed.security.UserDetailsImpl;
import com.example.newspeed.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CommentController {

    @Autowired
    private CommentService commentService;

    @PostMapping("/api/content/{id}/comment")
    public ResponseEntity<String> addComment(@PathVariable Long id,
        @RequestBody CommentRequest request,
        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        Long commentId = commentService.create(id, request, userDetails);
        return ResponseEntity.ok(commentId + " Comment created");
    }

    @GetMapping("/api/comment/{comment_id}")
    public ResponseEntity<CommentGetResponse> getComment(@PathVariable Long comment_id) {
        CommentGetResponse comment = commentService.get(comment_id);
        return ResponseEntity.ok(comment);
    }

    @PutMapping("/api/comment/{comment_id}")
    public ResponseEntity<String> update(@PathVariable Long comment_id, @RequestBody CommentRequest request, @AuthenticationPrincipal UserDetailsImpl userDetails){
        Long id = commentService.update(comment_id, request, userDetails);
        return ResponseEntity.ok(id + " Comment updated");
    }

    @DeleteMapping("/api/comment/{comment_id}")
    public ResponseEntity<String> delete(@PathVariable Long comment_id, @AuthenticationPrincipal UserDetailsImpl userDetails){
        Long id = commentService.delete(comment_id, userDetails);
        return ResponseEntity.ok(id + " Comment deleted");
    }
}
