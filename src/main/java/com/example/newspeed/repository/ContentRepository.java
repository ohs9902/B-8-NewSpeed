package com.example.newspeed.repository;

import com.example.newspeed.entity.Content;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ContentRepository extends JpaRepository<Content, Long> {
    List<Content> findAllByOrderByCreatedDateDesc();
}
