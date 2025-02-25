package com.example.ticket_support.services;

import com.example.ticket_support.dtos.CommentRequest;

public interface CommentService {
    void createComment(Long ticketId,CommentRequest request);
}
