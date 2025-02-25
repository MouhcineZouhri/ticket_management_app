package com.example.ticket_support.repositories;

import com.example.ticket_support.entities.TicketLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketLogRepository extends JpaRepository<TicketLog, Long> {
}
