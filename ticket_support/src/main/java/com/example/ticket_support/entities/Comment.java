package com.example.ticket_support.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor @NoArgsConstructor
@Builder @Getter @Setter
@Table(name = "comments")
public class Comment {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String text;

    @ManyToOne
    private Ticket ticket;

    @ManyToOne
    private User user;
}
