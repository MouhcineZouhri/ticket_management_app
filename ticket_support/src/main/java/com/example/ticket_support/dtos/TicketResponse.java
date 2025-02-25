package com.example.ticket_support.dtos;

import com.example.ticket_support.enums.Category;
import com.example.ticket_support.enums.Priority;
import com.example.ticket_support.enums.TicketStatus;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor @NoArgsConstructor
@Builder
@Getter @Setter
public class TicketResponse {
    private Long id;
    private String title;
    private String description;
    private Priority priority;
    private Category category;
    private TicketStatus ticketStatus;
    private LocalDateTime createdAt;
}
