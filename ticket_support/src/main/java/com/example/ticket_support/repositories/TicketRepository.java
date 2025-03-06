package com.example.ticket_support.repositories;

import com.example.ticket_support.dtos.TicketResponse;
import com.example.ticket_support.entities.User;
import com.example.ticket_support.enums.TicketStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ticket_support.entities.Ticket;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket, Long>{
    List<Ticket> findTicketsByTicketStatus(TicketStatus ticketStatus);

    @Query("SELECT NEW com.example.ticket_support.dtos.TicketResponse(t.id, t.title,t.description,t.priority,t.category,t.ticketStatus,t.createdAt,o.name) FROM Ticket t INNER JOIN t.owner o WHERE (t.ticketStatus = :ticketStatus OR :ticketStatus is NULL)")
    List<TicketResponse> findAllTickets(TicketStatus ticketStatus);

    @Query("SELECT NEW com.example.ticket_support.dtos.TicketResponse(t.id, t.title,t.description,t.priority,t.category,t.ticketStatus,t.createdAt,o.name) FROM Ticket t INNER JOIN t.owner o WHERE (t.ticketStatus = :ticketStatus OR :ticketStatus is NULL) and o = :user ")
    List<TicketResponse> findTicketsByOwnerAndTicketStatus(User user, TicketStatus ticketStatus);
}
