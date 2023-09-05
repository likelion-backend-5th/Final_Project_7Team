package com.likelion.catdogpia.repository;

import com.likelion.catdogpia.domain.entity.community.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
