package com.example.ticket_support.exceptions;

import com.example.ticket_support.enums.TicketStatus;

public class TicketStatusIsSameException extends RuntimeException{
    public TicketStatusIsSameException(TicketStatus ticketStatus){
        super("Ticket status Have the same status = " + ticketStatus);
    }
}
