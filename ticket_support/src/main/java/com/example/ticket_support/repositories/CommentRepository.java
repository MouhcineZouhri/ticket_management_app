package com.example.ticket_support.repositories;

import com.example.ticket_support.entities.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
