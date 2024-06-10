package com.example.newspeed.repository;

import com.example.newspeed.entity.Content;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface ContentRepository extends JpaRepository<Content, Long> {

    Page<Content> findByCreatedDateBetween(LocalDateTime createdDate, LocalDateTime updatedDate, Pageable pageable);
    Page<Content> findAllByOrderByLikesDesc(Pageable pageable);
}
