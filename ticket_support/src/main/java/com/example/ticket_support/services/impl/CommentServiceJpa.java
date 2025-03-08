package com.example.ticket_support.services.impl;

import com.example.ticket_support.dtos.CommentRequest;
import com.example.ticket_support.entities.Comment;
import com.example.ticket_support.entities.Ticket;
import com.example.ticket_support.entities.User;
import com.example.ticket_support.exceptions.TicketNotFoundException;
import com.example.ticket_support.repositories.CommentRepository;
import com.example.ticket_support.repositories.TicketRepository;
import com.example.ticket_support.repositories.UserRepository;
import com.example.ticket_support.services.CommentService;
import jakarta.transaction.Transactional;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class CommentServiceJpa implements CommentService {
    private final TicketRepository ticketRepository;

    private final CommentRepository commentRepository;

    private final UserRepository userRepository;

    public CommentServiceJpa(TicketRepository ticketRepository, CommentRepository commentRepository, UserRepository userRepository) {
        this.ticketRepository = ticketRepository;
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void createComment(Long ticketId, String email, CommentRequest request) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new TicketNotFoundException(ticketId));

        User creator = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User Not Found"));

        Comment comment = Comment.builder()
                .text(request.getText())
                .ticket(ticket)
                .user(creator)
                .build();

        commentRepository.save(comment);
    }
}
