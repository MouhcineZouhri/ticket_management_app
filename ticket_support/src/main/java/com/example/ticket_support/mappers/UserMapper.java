package com.example.ticket_support.mappers;


import com.example.ticket_support.dtos.users.UserRequest;
import com.example.ticket_support.dtos.users.UserResponse;
import com.example.ticket_support.entities.User;

public interface UserMapper {
    UserResponse userResponseFromUser(User user);
    User userFromUserRequest(UserRequest request);
}
