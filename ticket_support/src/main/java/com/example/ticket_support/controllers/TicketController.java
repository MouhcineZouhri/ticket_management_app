package com.example.ticket_support.controllers;

import com.example.ticket_support.dtos.CommentRequest;
import com.example.ticket_support.dtos.TicketRequest;
import com.example.ticket_support.dtos.TicketResponse;
import com.example.ticket_support.dtos.TicketResponseDetails;
import com.example.ticket_support.dtos.tickets.TicketChangeStatusRequest;
import com.example.ticket_support.enums.Role;
import com.example.ticket_support.enums.TicketStatus;
import com.example.ticket_support.security.SecurityUser;
import com.example.ticket_support.services.CommentService;
import com.example.ticket_support.services.TicketService;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tickets")
public class TicketController {
    private final TicketService ticketService;

    private final CommentService commentService;

    public TicketController(TicketService ticketService, CommentService commentService) {
        this.ticketService = ticketService;
        this.commentService = commentService;
    }

    /*  tickets for users
        if an employee will get only their tickets.
        if it_support get all tickets.
        filtered by ticket_status if it provided.
    */
    @GetMapping
    public ResponseEntity<List<TicketResponse>> getTickets(@RequestParam(required = false, name = "ticket_status") String ticketStatus) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Role role = authentication.getAuthorities()
                .stream()
                .findFirst().map(grantedAuthority -> Role.valueOf(grantedAuthority.getAuthority()))
                .orElse(null);

        TicketStatus status = ticketStatus == null ? null : TicketStatus.valueOf(ticketStatus.toUpperCase());

        if (role == Role.EMPLOYEE) {
            UserDetails user = (UserDetails) authentication.getPrincipal();
            List<TicketResponse> ownerTickets = ticketService.getOwnerTickets(user.getUsername(), status);
            return ResponseEntity.ok(ownerTickets);
        }

        List<TicketResponse> tickets = ticketService.getTickets(status);
        return ResponseEntity.ok(tickets);
    }

    @PostMapping
    public ResponseEntity<TicketResponse> createTicket(@RequestBody TicketRequest request) {
        SecurityUser user =(SecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        TicketResponse response = ticketService.createTicket(user.getUsername(),request);
        return ResponseEntity.status(201).body(response);
    }

    @GetMapping("/{id}")
    @PreAuthorize("@ticketSecurityUtils.isTicketOwner(#id) || hasAuthority('ITSUPPORT')")
    public ResponseEntity<TicketResponseDetails> getTicketById(@PathVariable Long id) {
        TicketResponseDetails response = ticketService.getTicketById(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ITSUPPORT')")
    public ResponseEntity<TicketResponse> changeTicketStatus(@PathVariable Long id, @RequestBody TicketChangeStatusRequest request) {
        TicketResponse response = ticketService.changeTicketStatus(id, request.getTicketStatus());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/comments")
    @PreAuthorize("hasAuthority('ITSUPPORT')")
    public ResponseEntity addCommentToTicket(@PathVariable Long id, @RequestBody CommentRequest request) {
        commentService.createComment(id, request);
        return ResponseEntity.ok().build();
    }
}

