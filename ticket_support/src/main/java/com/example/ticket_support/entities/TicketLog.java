package com.example.ticket_support.entities;

import com.example.ticket_support.enums.TicketStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@AllArgsConstructor @NoArgsConstructor
@Builder @Getter @Setter
@Table(name = "ticket_logs")
public class TicketLog {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "ticket_status")
    private TicketStatus ticketStatus;

    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime createdAt;

    @ManyToOne
    private Ticket ticket;
}
