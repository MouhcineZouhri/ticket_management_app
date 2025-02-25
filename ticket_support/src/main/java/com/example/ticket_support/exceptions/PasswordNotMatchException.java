package com.example.ticket_support.exceptions;

public class PasswordNotMatchException extends RuntimeException{
    public PasswordNotMatchException(){
        super("Password not match");
    }
}
