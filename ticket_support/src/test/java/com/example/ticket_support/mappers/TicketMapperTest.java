package com.example.ticket_support.mappers;

import com.example.ticket_support.dtos.TicketResponse;
import com.example.ticket_support.dtos.TicketResponseDetails;
import com.example.ticket_support.entities.Comment;
import com.example.ticket_support.entities.Ticket;
import com.example.ticket_support.entities.TicketLog;
import com.example.ticket_support.entities.User;
import com.example.ticket_support.enums.Category;
import com.example.ticket_support.enums.Priority;
import com.example.ticket_support.enums.TicketStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

class TicketMapperTest {

    private TicketMapper ticketMapper;

    @BeforeEach
    void beforeEach(){
        ticketMapper = new TicketMapperManual();
    }

    @Test
    void ticketResponseFromTicket_ShouldMapCorrectly() {
        // Given
        Ticket ticket = Ticket.builder()
                .title("Ticket 1")
                .description("Description 1")
                .priority(Priority.HIGH)
                .ticketStatus(TicketStatus.NEW)
                .category(Category.HARDWARE)
                .createdAt(LocalDateTime.of(2024, 2, 23, 10, 30))
                .build();


        // When
        TicketResponse response = ticketMapper.ticketResponseFromTicket(ticket);

        // Then
        assertThat(response).isNotNull();
        assertThat(ticket.getId()).isEqualTo(response.getId());

        assertThat(ticket.getId()).isEqualTo(response.getId());
        assertThat(ticket.getTitle()).isEqualTo(response.getTitle());
        assertThat(ticket.getDescription()).isEqualTo(response.getDescription());
        assertThat(ticket.getTicketStatus()).isEqualTo(response.getTicketStatus());
        assertThat(ticket.getCreatedAt()).isEqualTo(response.getCreatedAt());
        assertThat(ticket.getPriority()).isEqualTo(response.getPriority());
    }


    @Test
    void ticketResponseDetailsFromTicket_ShouldMapCorrectly() {
        // Given
        Ticket ticket = Ticket.builder()
                .title("Ticket 1")
                .description("Description 1")
                .priority(Priority.HIGH)
                .category(Category.HARDWARE)
                .ticketStatus(TicketStatus.NEW)
                .owner(User.builder().name("mohsin").build())
                .createdAt(LocalDateTime.of(2024, 2, 23, 10, 30))
                .build();

        Comment comment1 = new Comment();
        comment1.setText("Customer reported the issue");

        Comment comment2 = new Comment();
        comment2.setText("Issue escalated to payment team");

        TicketLog log1 = new TicketLog();
        log1.setTicketStatus(TicketStatus.NEW);

        TicketLog log2 = new TicketLog();
        log2.setTicketStatus(TicketStatus.IN_PROGRESS);

        ticket.setComments(List.of(comment1, comment2));
        ticket.setTicketLogs(List.of(log1, log2));

        // When
        TicketResponseDetails response = ticketMapper.ticketResponseDetailsFromTicket(ticket);

        // Then
        assertThat(response).isNotNull();
        assertThat(ticket.getId()).isEqualTo(response.getId());

        assertThat(ticket.getId()).isEqualTo(response.getId());
        assertThat(ticket.getTitle()).isEqualTo(response.getTitle());
        assertThat(ticket.getDescription()).isEqualTo(response.getDescription());
        assertThat(ticket.getTicketStatus()).isEqualTo(response.getTicketStatus());
        assertThat(ticket.getCreatedAt()).isEqualTo(response.getCreatedAt());
        assertThat(ticket.getPriority()).isEqualTo(response.getPriority());
        assertThat(response.getCreatorName()).isEqualTo("mohsin");

        assertThat(response.getComments()).containsExactly("Customer reported the issue", "Issue escalated to payment team");
        assertThat(response.getTicketStatusHistory()).containsExactly(TicketStatus.NEW,TicketStatus.IN_PROGRESS);
    }

}