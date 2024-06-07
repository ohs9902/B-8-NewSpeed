package com.example.newspeed.repository;

import com.example.newspeed.entity.Content;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ContentRepository extends JpaRepository<Content, Long> {
    List<Content> findAllByOrderByCreatedDateDesc();
    Page<Content> findByCreatedDateBetween(LocalDateTime createdDate, LocalDateTime updatedDate, Pageable pageable);
}
