package com.example.newspeed.dto;

import com.example.newspeed.entity.Content;
import com.example.newspeed.entity.User;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ContentDto {

    private Long id;

    private User user;

    private String content;

    private LocalDateTime createdDate;

    private LocalDateTime updatedDate;

    public ContentDto(Content content) {
        this.id = content.getId();
        this.user = content.getUser();
        this.content = content.getContent();
        this.createdDate = content.getCreatedDate();
        this.updatedDate = content.getUpdatedDate();
    }
}
