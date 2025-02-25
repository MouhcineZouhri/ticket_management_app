package com.example.ticket_support.exceptions;

import com.example.ticket_support.enums.TicketStatus;

public class TicketStatusSameException extends RuntimeException{

    public TicketStatusSameException(TicketStatus status){
        super("Ticket status is the same " + status.toString());
    }
}
