package com.example.ticket_support.dtos;

import com.example.ticket_support.enums.Category;
import com.example.ticket_support.enums.Priority;
import com.example.ticket_support.enums.TicketStatus;
import lombok.*;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@AllArgsConstructor @NoArgsConstructor
@Builder
@Getter @Setter
public class TicketResponseDetails {
    private Long id;
    private String title;
    private String description;
    private Priority priority;
    private Category category;
    private TicketStatus ticketStatus;
    private LocalDateTime createdAt;
    private List<TicketStatus> ticketStatusHistory;
    private List<String> comments;
}
