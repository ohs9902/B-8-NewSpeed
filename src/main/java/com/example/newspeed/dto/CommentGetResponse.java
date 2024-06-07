package com.example.newspeed.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentGetResponse {
    private Long id;
    private Long userId;
    private String comment;

    public CommentGetResponse(Long id, Long userId, String comment) {
        this.id = id;
        this.userId = userId;
        this.comment = comment;
    }
}
