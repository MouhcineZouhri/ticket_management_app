package com.example.ticket_support.exceptions;

public class UserAlreadyFound extends RuntimeException{

    public  UserAlreadyFound(String email){
        super("User with email " + email + " Already Exist");
    }
}
