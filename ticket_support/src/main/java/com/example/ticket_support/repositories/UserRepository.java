package com.example.ticket_support.repositories;

import com.example.ticket_support.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    @Query("SELECT u FROM User u INNER JOIN Ticket t ON t.owner = u WHERE t.id = :ticketId")
    User findOwnerOfTicket(Long ticketId);

}

