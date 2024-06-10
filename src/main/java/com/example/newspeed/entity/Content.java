package com.example.newspeed.entity;

import com.example.newspeed.dto.ContentDto;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Content {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Lob
    private String content;

    private LocalDateTime createdDate;

    private LocalDateTime updatedDate;

    @OneToMany(mappedBy = "content",cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<Like> likeList = new ArrayList<>();

    private Integer likes = 0;

    public void addLike(Like like) {
        likeList.add(like);
        like.setContent(this);
        likes++;
    }

    public void removeLike(Like like) {
        likeList.remove(like);
        like.setContent(null);
        likes--;
    }
}
