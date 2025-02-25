package com.example.ticket_support.dtos;

import com.example.ticket_support.enums.Category;
import com.example.ticket_support.enums.Priority;
import lombok.Builder;
import lombok.Getter;

// use to create a ticket
@Getter
@Builder
public class TicketRequest {
    private String title;
    private String description;
    private Priority priority;
    private Category category;
}
