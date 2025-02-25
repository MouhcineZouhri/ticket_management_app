package org.example.models;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.*;
import org.example.enums.Category;
import org.example.enums.Priority;
import org.example.enums.TicketStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor @NoArgsConstructor
@Builder
@Getter @Setter
@ToString
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

    public static TicketResponseDetails parse(JsonNode node){
        System.out.println(node);
        List<String> comments = new ArrayList<>();
        JsonNode jsonComments = node.get("comments");
        for (int i = 0; i < jsonComments.size(); i++) {
            comments.add(jsonComments.get(i).toString());
        }

        List<TicketStatus> ticketStatuses = new ArrayList<>();
        JsonNode jsonTicketStatus = node.get("ticketStatusHistory");
        for (int i = 0; i < jsonTicketStatus.size(); i++) {
            ticketStatuses.add(TicketStatus.valueOf(jsonTicketStatus.get(i).toString().replace("\"", "")));
        }

        return  TicketResponseDetails.builder()
                .id(node.get("id").asLong())
                .title(node.get("title").toString().replace("\"", ""))
                .description(node.get("description").toString().replace("\"", ""))
                .priority(Priority.valueOf(node.get("priority").toString().replace("\"", "")))
                .category(Category.valueOf(node.get("category").toString().replace("\"", "")))
                .ticketStatus(TicketStatus.valueOf(node.get("ticketStatus").toString().replace("\"", "")))
                .createdAt(LocalDateTime.parse(node.get("createdAt").toString().replace("\"", "")))
                .comments(comments)
                .ticketStatusHistory(ticketStatuses)
                .build();
    }
}
