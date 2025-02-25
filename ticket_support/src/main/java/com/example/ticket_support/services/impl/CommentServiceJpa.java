package com.example.ticket_support.services.impl;

import com.example.ticket_support.dtos.CommentRequest;
import com.example.ticket_support.entities.Comment;
import com.example.ticket_support.entities.Ticket;
import com.example.ticket_support.exceptions.TicketNotFoundException;
import com.example.ticket_support.repositories.CommentRepository;
import com.example.ticket_support.repositories.TicketRepository;
import com.example.ticket_support.services.CommentService;
import org.springframework.stereotype.Service;

@Service
public class CommentServiceJpa implements CommentService {
    private final TicketRepository ticketRepository;

    private final CommentRepository commentRepository;

    public CommentServiceJpa(TicketRepository ticketRepository, CommentRepository commentRepository) {
        this.ticketRepository = ticketRepository;
        this.commentRepository = commentRepository;
    }

    @Override
    public void createComment(Long ticketId, CommentRequest request) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new TicketNotFoundException(ticketId));

        Comment comment = Comment.builder()
                .text(request.getText())
                .ticket(ticket)
                .build();

        commentRepository.save(comment);
    }
}
