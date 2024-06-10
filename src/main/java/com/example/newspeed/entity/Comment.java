package com.example.newspeed.entity;


import com.example.newspeed.dto.ContentDto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Comment extends Timestamped{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne
    @JoinColumn(name = "content_id")
    private Content news;


    private Long userId;

    @Column(nullable = false)
    private String comment;


    private Long likeCnt;

    public Comment(Long userId, String comment, Content news) {
        this.userId = userId;
        this.comment = comment;
        this.news = news;
    }

}
