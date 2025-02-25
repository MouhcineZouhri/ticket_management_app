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

	@Bean
	CommandLineRunner commandLineRunner(TicketRepository ticketRepository,
										TicketLogRepository ticketLogRepository,
										UserRepository userRepository,
										PasswordEncoder passwordEncoder
										) {
		return (args) -> {

			User user = User.builder()
					.email("mohsin@gmail.com")
					.name("mohsin")
					.role(Role.EMPLOYEE)
					.password(passwordEncoder.encode("password"))
					.build();

			User user1 = User.builder()
					.email("omar@gmail.com")
					.name("mohsin")
					.role(Role.ITSUPPORT)
					.password(passwordEncoder.encode("password"))
					.build();

			userRepository.save(user);
			userRepository.save(user1);

			Ticket ticket = Ticket.builder()
					.title("Title 1")
					.description("Description 1")
					.category(Category.SOFTWARE)
					.ticketStatus(TicketStatus.NEW)
					.priority(Priority.HIGH)
					.createdAt(LocalDateTime.now())
					.owner(user)
					.build();

			Ticket ticket1 = Ticket.builder()
					.title("Title 1")
					.description("Description 1")
					.category(Category.SOFTWARE)
					.ticketStatus(TicketStatus.NEW)
					.priority(Priority.HIGH)
					.createdAt(LocalDateTime.now())
					.owner(user1)
					.build();

			TicketLog ticketLog = TicketLog.builder()
					.ticket(ticket)
					.createdAt(LocalDateTime.now())
					.ticketStatus(TicketStatus.NEW)
					.build();

			TicketLog ticketLog1 = TicketLog.builder()
					.ticket(ticket1)
					.createdAt(LocalDateTime.now())
					.ticketStatus(TicketStatus.NEW)
					.build();

			ticketRepository.save(ticket);
			ticketLogRepository.save(ticketLog);

			ticketRepository.save(ticket1);
			ticketLogRepository.save(ticketLog1);
		};
	}
	
}
