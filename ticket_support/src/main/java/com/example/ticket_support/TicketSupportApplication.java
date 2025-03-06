package com.example.ticket_support;

import com.example.ticket_support.entities.TicketLog;
import com.example.ticket_support.entities.User;
import com.example.ticket_support.enums.Category;
import com.example.ticket_support.enums.Priority;
import com.example.ticket_support.enums.Role;
import com.example.ticket_support.enums.TicketStatus;
import com.example.ticket_support.repositories.TicketLogRepository;
import com.example.ticket_support.repositories.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.example.ticket_support.entities.Ticket;
import com.example.ticket_support.repositories.TicketRepository;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.time.LocalTime;

@SpringBootApplication
public class TicketSupportApplication {

	public static void main(String[] args) {
		SpringApplication.run(TicketSupportApplication.class, args);
	}

}
