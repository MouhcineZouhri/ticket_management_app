package com.example.ticket_support.services;

import com.example.ticket_support.dtos.users.UserRequest;
import com.example.ticket_support.dtos.users.UserResponse;

public interface AuthService {
    UserResponse createUser(UserRequest request);

    UserResponse getUser();
}
