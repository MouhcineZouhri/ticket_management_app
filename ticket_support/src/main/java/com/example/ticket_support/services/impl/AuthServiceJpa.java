package com.example.ticket_support.services.impl;

import com.example.ticket_support.dtos.users.UserRequest;
import com.example.ticket_support.dtos.users.UserResponse;
import com.example.ticket_support.entities.User;
import com.example.ticket_support.exceptions.PasswordNotMatchException;
import com.example.ticket_support.exceptions.UserAlreadyFound;
import com.example.ticket_support.mappers.UserMapper;
import com.example.ticket_support.repositories.UserRepository;
import com.example.ticket_support.services.AuthService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceJpa implements AuthService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public AuthServiceJpa(UserRepository userRepository, UserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserResponse createUser(UserRequest request) {
        if (!request.getPassword().equals(request.getConfirmPassword())){
            throw new PasswordNotMatchException();
        }

        User savedUser = userRepository.findByEmail(request.getEmail()).orElse(null);

        if (savedUser != null){
            throw new UserAlreadyFound(request.getEmail());
        }

        User user = userMapper.userFromUserRequest(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        userRepository.save(user);

        return userMapper.userResponseFromUser(user);
    }

    @Override
    public UserResponse getUser() {
        UserDetails currUser = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userRepository.findByEmail(currUser.getUsername()).
                orElse(null);

        return userMapper.userResponseFromUser(user);
    }


}
