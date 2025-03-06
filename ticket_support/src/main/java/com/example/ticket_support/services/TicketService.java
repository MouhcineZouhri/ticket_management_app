package com.example.ticket_support.services;

import com.example.ticket_support.dtos.TicketRequest;
import com.example.ticket_support.dtos.TicketResponse;
import com.example.ticket_support.dtos.TicketResponseDetails;
import com.example.ticket_support.dtos.tickets.TicketChangeStatusRequest;
import com.example.ticket_support.entities.User;
import com.example.ticket_support.enums.TicketStatus;

import java.util.List;

public interface TicketService {
    List<TicketResponse> getTickets(TicketStatus ticketStatus);

    List<TicketResponse> getOwnerTickets(String username,TicketStatus ticketStatus);

    TicketResponseDetails getTicketById(Long id);

    TicketResponse createTicket(String username,TicketRequest request);

    TicketResponse changeTicketStatus(Long id , TicketStatus status);

}
