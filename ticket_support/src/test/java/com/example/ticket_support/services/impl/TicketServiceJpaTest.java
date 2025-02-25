package com.example.ticket_support.services.impl;

import com.example.ticket_support.dtos.TicketRequest;
import com.example.ticket_support.dtos.TicketResponse;
import com.example.ticket_support.dtos.TicketResponseDetails;
import com.example.ticket_support.entities.Ticket;
import com.example.ticket_support.entities.TicketLog;
import com.example.ticket_support.entities.User;
import com.example.ticket_support.enums.TicketStatus;
import com.example.ticket_support.exceptions.TicketNotFoundException;
import com.example.ticket_support.exceptions.TicketStatusIsSameException;
import com.example.ticket_support.mappers.TicketMapper;
import com.example.ticket_support.repositories.TicketLogRepository;
import com.example.ticket_support.repositories.TicketRepository;
import com.example.ticket_support.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

class TicketServiceJpaTest {

    @Mock
    private TicketRepository ticketRepository;

    @Mock
    private TicketLogRepository ticketLogRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private TicketMapper ticketMapper;

    @InjectMocks
    private TicketServiceJpa ticketService;

    private Ticket ticket;
    private TicketLog ticketLog;
    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Create a mock Ticket
        ticket = Ticket.builder()
                .id(1L)
                .title("Test Ticket")
                .description("Test description")
                .ticketStatus(TicketStatus.NEW)
                .createdAt(LocalDateTime.of(2024, 2, 23, 10, 30))
                .build();

        // Create a mock TicketLog
        ticketLog = TicketLog.builder()
                .ticket(ticket)
                .ticketStatus(TicketStatus.NEW)
                .createdAt(LocalDateTime.now())
                .build();

        // Create a mock User
        user = User
                .builder()
                .email("mohsin@gmail.com")
                .build();
    }

    @Test
    void getTickets_ShouldReturnListOfTickets_WhenStatusIsProvided() {
        // Given
        TicketStatus ticketStatus = TicketStatus.NEW;
        when(ticketRepository.findTicketsByTicketStatus(ticketStatus))
                .thenReturn(Arrays.asList(ticket));

        when(ticketMapper.ticketResponseFromTicket(ticket)).
                thenReturn(
                        TicketResponse
                                .builder()
                                .id(1L)
                                .title("Test Ticket")
                                .description("Test description")
                                .ticketStatus(TicketStatus.NEW)
                                .build()
                );

        // When
        List<TicketResponse> response = ticketService.getTickets(ticketStatus);

        // Then
        assertThat(response)
                .isNotNull()
                .hasSize(1);

        assertThat(response.getFirst().getId()).isEqualTo(1L);
        assertThat(response.getFirst().getTicketStatus()).isEqualTo(TicketStatus.NEW);

        verify(ticketRepository, times(1))
                .findTicketsByTicketStatus(ticketStatus);
        verify(ticketRepository, times(0)).findAll();
    }

    @Test
    void getOwnerTickets_ShouldReturnTicketsForUser() {
        // Given
        String email = "mohsin@gmail.com";
        TicketStatus ticketStatus = TicketStatus.NEW;
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(ticketRepository.findTicketsByOwnerAndTicketStatus(user, ticketStatus)).thenReturn(Arrays.asList(ticket));
        when(ticketMapper.ticketResponseFromTicket(ticket)).
                thenReturn(
                        TicketResponse
                                .builder()
                                .id(1L)
                                .title("Test Ticket")
                                .description("Test description")
                                .ticketStatus(TicketStatus.NEW)
                                .build()
                );

        // When
        var response = ticketService.getOwnerTickets(email, ticketStatus);

        // Then
        assertThat(response)
                .isNotNull()
                .hasSize(1);

        assertThat(response.getFirst().getId()).isEqualTo(ticket.getId());
        assertThat(response.getFirst().getTitle()).isEqualTo(ticket.getTitle());


        verify(userRepository, times(1)).findByEmail(email);
        verify(ticketRepository, times(1)).findTicketsByOwnerAndTicketStatus(user, ticketStatus);
    }

    @Test
    void getTicketById_ShouldReturnTicketDetails_WhenTicketExists() {
        // Given
        when(ticketRepository.findById(1L)).thenReturn(Optional.of(ticket));
        when(ticketMapper.ticketResponseDetailsFromTicket(ticket)).thenReturn(
                TicketResponseDetails.builder()
                        .id(1L)
                        .title("Test Ticket")
                        .description("Test description")
                        .build());

        // When
        TicketResponseDetails responseDetails = ticketService.getTicketById(1L);

        // Then
        assertThat(responseDetails)
                .isNotNull();

        assertThat(responseDetails.getId()).isEqualTo(ticket.getId());
        assertThat(responseDetails.getTitle()).isEqualTo(ticket.getTitle());

        verify(ticketRepository, times(1)).findById(1L);
    }

    @Test
    void getTicketById_ShouldThrowTicketNotFoundException_WhenTicketDoesNotExist() {
        // Given
        when(ticketRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> ticketService.getTicketById(1L))
                .isInstanceOf(TicketNotFoundException.class)
                .hasMessage("Ticket with id " + 1L + " NOT FOUND");

        verify(ticketRepository, times(1)).findById(1L);
    }

    @Test
    void createTicket_ShouldCreateTicket() {
        // Given
        TicketRequest request = TicketRequest.builder()
                .title("Test Ticket")
                .description("Test description")
                .build();

        when(ticketRepository.save(any(Ticket.class))).thenReturn(ticket);
        when(ticketLogRepository.save(any(TicketLog.class))).thenReturn(ticketLog);
        when(ticketMapper.ticketResponseFromTicket(ticket)).
                thenReturn(
                        TicketResponse.builder()
                                .id(1L)
                                .title("Test Ticket")
                                .description("Test description")
                                .build()
                );

        // When
        TicketResponse response = ticketService.createTicket(request);

        // Then
        assertThat(response)
                .isNotNull();

        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getTitle()).isEqualTo("Test Ticket");


        verify(ticketRepository, times(1)).save(any(Ticket.class));
        verify(ticketLogRepository, times(1)).save(any(TicketLog.class));
    }

    @Test
    void changeTicketStatus_ShouldChangeStatusAndReturnUpdatedTicket() {
        // Given
        TicketStatus ticketStatus = TicketStatus.RESOLVED;
        when(ticketRepository.findById(1L)).thenReturn(Optional.of(ticket));
        when(ticketMapper.ticketResponseFromTicket(ticket)).thenReturn(
                TicketResponse.builder()
                        .title("Test Ticket")
                        .description("Test description")
                        .ticketStatus(ticketStatus)
                        .build());

        // When
        TicketResponse response = ticketService.changeTicketStatus(1L, ticketStatus);

        // Then
        assertThat(response)
                .isNotNull();

        assertThat(response.getTicketStatus()).isEqualTo(ticketStatus);

        verify(ticketRepository, times(1)).findById(1L);
        verify(ticketLogRepository, times(1)).save(any(TicketLog.class));
    }

    @Test
    void changeTicketStatus_ShouldThrowTicketStatusIsSameException_WhenTicketAlreadyHaveTheSameStatus() {
        // Given
        TicketStatus ticketStatus = TicketStatus.NEW;
        when(ticketRepository.findById(1L)).thenReturn(Optional.of(ticket));
        when(ticketMapper.ticketResponseFromTicket(ticket)).thenReturn(
                TicketResponse.builder()
                        .title("Test Ticket")
                        .description("Test description")
                        .ticketStatus(ticketStatus)
                        .build());

        // When & Then
        assertThatThrownBy(() -> ticketService.changeTicketStatus(1L, ticketStatus))
                .isInstanceOf(TicketStatusIsSameException.class)
                .hasMessage("Ticket status Have the same status = "+ ticketStatus);
    }
}
