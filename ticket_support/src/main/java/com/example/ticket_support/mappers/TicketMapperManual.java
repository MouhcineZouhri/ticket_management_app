package com.example.ticket_support.mappers;

import com.example.ticket_support.dtos.TicketResponse;
import com.example.ticket_support.dtos.TicketResponseDetails;
import com.example.ticket_support.entities.Comment;
import com.example.ticket_support.entities.Ticket;
import com.example.ticket_support.entities.TicketLog;
import org.springframework.stereotype.Component;

@Component
public class TicketMapperManual implements TicketMapper {

    public TicketResponse ticketResponseFromTicket(Ticket ticket){
        return  TicketResponse.builder()
                .id(ticket.getId())
                .title(ticket.getTitle())
                .category(ticket.getCategory())
                .description(ticket.getDescription())
                .ticketStatus(ticket.getTicketStatus())
                .createdAt(ticket.getCreatedAt())
                .priority(ticket.getPriority())
                .build();
    }

    public  TicketResponseDetails ticketResponseDetailsFromTicket(Ticket ticket){
        return TicketResponseDetails.builder()
                .id(ticket.getId())
                .title(ticket.getTitle())
                .description(ticket.getDescription())
                .ticketStatus(ticket.getTicketStatus())
                .createdAt(ticket.getCreatedAt())
                .priority(ticket.getPriority())
                .category(ticket.getCategory())
                .comments(ticket.getComments().stream().map(Comment::getText).toList())
                .ticketStatusHistory(ticket.getTicketLogs().stream().map(TicketLog::getTicketStatus).toList())
                .build();
    }

}
