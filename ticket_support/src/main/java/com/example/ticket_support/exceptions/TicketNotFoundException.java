package com.example.ticket_support.exceptions;

public class TicketNotFoundException extends RuntimeException{
    public TicketNotFoundException(Long id){
        super("Ticket with id " + id + " NOT FOUND");
    }
}
