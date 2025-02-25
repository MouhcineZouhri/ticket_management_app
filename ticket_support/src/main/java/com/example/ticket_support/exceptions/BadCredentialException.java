package com.example.ticket_support.exceptions;

public class BadCredentialException extends RuntimeException{
    BadCredentialException(){
        super("Bad Credential");
    }
}
