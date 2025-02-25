package com.example.ticket_support.dtos.tickets;

import com.example.ticket_support.enums.TicketStatus;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class TicketChangeStatusRequest {
    @JsonProperty("ticket_status")
    TicketStatus ticketStatus;
}
