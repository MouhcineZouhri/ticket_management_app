package com.example.ticket_support.mappers;

import com.example.ticket_support.dtos.TicketResponse;
import com.example.ticket_support.dtos.TicketResponseDetails;
import com.example.ticket_support.entities.Ticket;

public interface TicketMapper {
    TicketResponse ticketResponseFromTicket(Ticket ticket);
    TicketResponseDetails ticketResponseDetailsFromTicket(Ticket ticket);
}
