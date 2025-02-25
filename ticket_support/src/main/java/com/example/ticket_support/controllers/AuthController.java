package com.example.ticket_support.controllers;

import com.example.ticket_support.dtos.users.UserRequest;
import com.example.ticket_support.dtos.users.UserResponse;
import com.example.ticket_support.services.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/api/users")
    public ResponseEntity<UserResponse> getUser(){
        return ResponseEntity.ok(authService.getUser());
    }

    @PostMapping("/register")
    ResponseEntity<UserResponse> createUser(@RequestBody UserRequest request){
        UserResponse userResponse = authService.createUser(request);
        return ResponseEntity.ok(userResponse);
    }


}
