package com.example.ticket_support.utils;

import com.example.ticket_support.entities.User;
import com.example.ticket_support.repositories.UserRepository;
import com.example.ticket_support.security.SecurityUser;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class TicketSecurityUtils {

    private final UserRepository userRepository;

    public TicketSecurityUtils(UserRepository userRepository1) {
        this.userRepository = userRepository1;
    }

    public boolean isTicketOwner(Long ticketId){
        UserDetails securityUser = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        User user  = userRepository.findOwnerOfTicket(ticketId);

        return user.getEmail().equals(securityUser.getUsername());
    }

}
