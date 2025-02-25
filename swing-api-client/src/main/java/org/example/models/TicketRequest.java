package org.example.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.enums.Category;
import org.example.enums.Priority;


@Builder
@AllArgsConstructor @NoArgsConstructor
@Getter
public class TicketRequest {
    private String title;
    private String description;
    private Priority priority;
    private Category category;
}
