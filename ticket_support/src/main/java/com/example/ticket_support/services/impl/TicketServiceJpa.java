package com.example.ticket_support.services.impl;

import com.example.ticket_support.dtos.TicketRequest;
import com.example.ticket_support.dtos.TicketResponse;
import com.example.ticket_support.dtos.TicketResponseDetails;
import com.example.ticket_support.dtos.tickets.TicketChangeStatusRequest;
import com.example.ticket_support.entities.Comment;
import com.example.ticket_support.entities.Ticket;
import com.example.ticket_support.entities.TicketLog;
import com.example.ticket_support.entities.User;
import com.example.ticket_support.enums.TicketStatus;
import com.example.ticket_support.exceptions.TicketNotFoundException;
import com.example.ticket_support.exceptions.TicketStatusIsSameException;
import com.example.ticket_support.mappers.TicketMapper;
import com.example.ticket_support.repositories.TicketLogRepository;
import com.example.ticket_support.repositories.TicketRepository;
import com.example.ticket_support.repositories.UserRepository;
import com.example.ticket_support.services.TicketService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class TicketServiceJpa implements TicketService {
    private final TicketRepository ticketRepository;

    private final TicketLogRepository ticketLogRepository;

    private final UserRepository userRepository;

    private final TicketMapper ticketMapper;

    public TicketServiceJpa(TicketRepository ticketRepository, TicketLogRepository ticketLogRepository, TicketMapper ticketMapper, UserRepository userRepository) {
        this.ticketRepository = ticketRepository;
        this.ticketLogRepository = ticketLogRepository;
        this.ticketMapper = ticketMapper;
        this.userRepository = userRepository;
    }

    @Override
    public List<TicketResponse> getTickets(TicketStatus ticketStatus) {
        if (ticketStatus != null)
            return ticketRepository.findTicketsByTicketStatus(ticketStatus)
                    .stream().map(
                            ticketMapper::ticketResponseFromTicket
                    ).toList();

        return ticketRepository.findAll().stream().map(
                ticketMapper::ticketResponseFromTicket
        ).toList();
    }

    @Override
    public List<TicketResponse> getOwnerTickets(String email, TicketStatus ticketStatus) {
        // always owner exist because function call after authentication
        User owner = userRepository.findByEmail(email).
                orElse(null);

        return ticketRepository
                .findTicketsByOwnerAndTicketStatus(owner, ticketStatus)
                .stream()
                .map(ticketMapper::ticketResponseFromTicket)
                .toList();
    }

    @Override
    public TicketResponseDetails getTicketById(Long id) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new TicketNotFoundException(id));

        return ticketMapper.ticketResponseDetailsFromTicket(ticket);
    }

    @Override
    public TicketResponse createTicket(TicketRequest request) {
        UserDetails currUser = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User owner = userRepository.findByEmail(currUser.getUsername()).
                orElse(null);

        Ticket ticket = Ticket.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .category(request.getCategory())
                .priority(request.getPriority())
                .ticketStatus(TicketStatus.NEW)
                .createdAt(LocalDateTime.now())
                .owner(owner)
                .build();

        TicketLog ticketLog = TicketLog.builder()
                .ticketStatus(ticket.getTicketStatus())
                .createdAt(ticket.getCreatedAt())
                .ticket(ticket)
                .build();

        Ticket savedTicket = ticketRepository.save(ticket);
        ticketLogRepository.save(ticketLog);

        return ticketMapper.ticketResponseFromTicket(savedTicket);
    }

    @Override
    public TicketResponse changeTicketStatus(Long id, TicketStatus status) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new TicketNotFoundException(id));

        if (ticket.getTicketStatus().equals(status)) {
            throw new TicketStatusIsSameException(status);
        }

        ticket.setTicketStatus(status);
        ticketRepository.save(ticket);

        ticketLogRepository.save(TicketLog.builder()
                .ticketStatus(status)
                .ticket(ticket)
                .build());

        return ticketMapper.ticketResponseFromTicket(ticket);
    }
}
