package com.example.newspeed.repository;

import com.example.newspeed.entity.Content;
import com.example.newspeed.entity.Like;
import com.example.newspeed.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like,Long> {
    Optional<Like> findByUserAndContent(User user, Content content);
}
