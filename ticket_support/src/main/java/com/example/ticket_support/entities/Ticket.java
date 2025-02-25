package com.example.ticket_support.entities;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import com.example.ticket_support.enums.Category;
import com.example.ticket_support.enums.Priority;

import com.example.ticket_support.enums.TicketStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
@Builder
public class Ticket {
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String title;
	
	private String description;

	// represent the current status of ticket
	@Column(name = "status")
	@Enumerated(EnumType.ORDINAL)
	private TicketStatus ticketStatus;
	
	@Enumerated(EnumType.ORDINAL)
	private Priority priority; 
	
	@Enumerated(EnumType.ORDINAL)
	private Category category;

	@Column(name = "created_at")
	@Temporal(value = TemporalType.TIMESTAMP)
	private LocalDateTime createdAt;

	@OneToMany(mappedBy = "ticket")
	private List<Comment> comments;

	@OneToMany(mappedBy = "ticket")
	private List<TicketLog> ticketLogs;

	@ManyToOne
	private User owner;
}
