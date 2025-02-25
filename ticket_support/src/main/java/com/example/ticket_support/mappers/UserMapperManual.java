package com.example.ticket_support.mappers;

import com.example.ticket_support.dtos.users.UserRequest;
import com.example.ticket_support.dtos.users.UserResponse;
import com.example.ticket_support.entities.User;

import org.springframework.stereotype.Component;

@Component
public class UserMapperManual implements UserMapper {

    @Override
    public UserResponse userResponseFromUser(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .role(user.getRole())
                .build();
    }

    @Override
    public User userFromUserRequest(UserRequest request) {
        return User.builder()
                .email(request.getEmail())
                .password(request.getPassword())
                .name(request.getName())
                .role(request.getRole())
                .build();
    }
}
