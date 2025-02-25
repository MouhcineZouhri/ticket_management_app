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

    @Query("SELECT t FROM Ticket t INNER JOIN t.owner o WHERE (t.ticketStatus = :ticketStatus OR :ticketStatus is NULL) and o = :user ")
    List<Ticket> findTicketsByOwnerAndTicketStatus(User user, TicketStatus ticketStatus);
}
