package com.example.ticket_support.controllers;

import com.example.ticket_support.entities.Ticket;
import com.example.ticket_support.entities.TicketLog;
import com.example.ticket_support.entities.User;
import com.example.ticket_support.enums.TicketStatus;
import com.example.ticket_support.repositories.TicketLogRepository;
import com.example.ticket_support.repositories.TicketRepository;
import com.example.ticket_support.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class TicketControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TicketLogRepository ticketLogRepository;

    private User user;

    @BeforeEach
    void setUp() {
        User savedUser = User.builder()
                .email("mohsin@example.com")
                .build();

        user = userRepository.save(savedUser);
    }

    @Test
    @WithMockUser(username = "mohsin@example.com", authorities = "EMPLOYEE")
    void getTickets_ShouldReturnListOfTickets_WhenStatusIsProvided() throws Exception {
        Ticket ticket = Ticket.builder()
                .title("Test Ticket")
                .description("Test description")
                .ticketStatus(TicketStatus.RESOLVED)
                .owner(user)
                .build();
        Ticket savedTicket = ticketRepository.save(ticket);

        mockMvc.perform(get("/api/tickets?ticket_status=RESOLVED"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(savedTicket.getId()))
                .andExpect(jsonPath("$[0].title").value("Test Ticket"))
                .andReturn();
    }

    @Test
    @WithMockUser(username = "mohsin@example.com", authorities = "EMPLOYEE")
    void createTicket_ShouldReturnCreatedTicket() throws Exception {
        mockMvc.perform(post("/api/tickets")
                        .contentType("application/json")
                        .content("{ \"title\": \"Test Ticket\", \"description\": \"Test description\" }"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Test Ticket"))
                .andReturn();
    }

    @Test
    @WithMockUser(username = "mohsin@example.com", authorities = "EMPLOYEE")
    void getTicketById_ShouldReturnTicketDetails_WhenTicketExists() throws Exception {
        Ticket ticket = Ticket.builder()
                .title("Test Ticket")
                .description("Test description")
                .ticketStatus(TicketStatus.NEW)
                .owner(user)
                .build();

        Ticket savedTicket = ticketRepository.save(ticket);

        mockMvc.perform(get("/api/tickets/" + savedTicket.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(savedTicket.getId()))
                .andExpect(jsonPath("$.title").value("Test Ticket"))
                .andReturn();
    }

    @Test
    @WithMockUser(username = "mohsin@example.com", authorities = "ITSUPPORT")
    void changeTicketStatus_ShouldReturnUpdatedTicket() throws Exception {
        Ticket ticket = Ticket.builder()
                .title("Test Ticket")
                .description("Test description")
                .ticketStatus(TicketStatus.NEW)
                .owner(user)
                .build();

        Ticket savedTicket = ticketRepository.save(ticket);

        mockMvc.perform(put("/api/tickets/" + savedTicket.getId())
                        .contentType("application/json")
                        .content("{\"ticket_status\":\"IN_PROGRESS\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ticketStatus").value("IN_PROGRESS"))
                .andReturn();
    }

    @Test
    @WithMockUser(username = "mohsin@example.com", authorities = "ITSUPPORT")
    void addCommentToTicket_ShouldReturnOk_WhenAuthorized() throws Exception {
        Ticket ticket = Ticket.builder()
                .title("Test Ticket")
                .description("Test description")
                .ticketStatus(TicketStatus.NEW)
                .owner(user)
                .build();

        Ticket savedTicket = ticketRepository.save(ticket);

        mockMvc.perform(post("/api/tickets/" + savedTicket.getId() + "/comments")
                        .contentType("application/json")
                        .content("{\"text\":\"Test comment\"}"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "mohsin@example.com", authorities = "EMPLOYEE")
    void addCommentToTicket_ShouldReturnForbidden_WhenNotAuthorized() throws Exception {
        Ticket ticket = Ticket.builder()
                .title("Test Ticket")
                .description("Test description")
                .ticketStatus(TicketStatus.NEW)
                .owner(user)
                .build();

        Ticket savedTicket = ticketRepository.save(ticket);

        mockMvc.perform(post("/api/tickets/" + savedTicket.getId() + "/comments")
                        .contentType("application/json")
                        .content("{\"text\":\"Test comment\"}"))
                .andExpect(status().isForbidden());
    }
}
