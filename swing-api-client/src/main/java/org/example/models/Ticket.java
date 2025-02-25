package org.example.models;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.*;
import org.example.enums.Category;
import org.example.enums.Priority;
import org.example.enums.TicketStatus;

import java.time.LocalDateTime;

@AllArgsConstructor @NoArgsConstructor
@Builder
@Getter @Setter
public class Ticket {
    private Long id;
    private String title;
    private String description;
    private Priority priority;
    private Category category;
    private TicketStatus ticketStatus;
    private LocalDateTime createdAt;

    public static Ticket parseTicket(JsonNode node){
        return Ticket.builder()
                .id(node.get("id").asLong())
                .title(node.get("title").toString().replace("\"", ""))
                .priority(Priority.valueOf(node.get("priority").toString().replace("\"", "")))
                .category(Category.valueOf(node.get("category").toString().replace("\"", "")))
                .ticketStatus(TicketStatus.valueOf(node.get("ticketStatus").toString().replace("\"", "")))
                .createdAt(LocalDateTime.parse(node.get("createdAt").toString().replace("\"", "")))
                .build();
    }
}
