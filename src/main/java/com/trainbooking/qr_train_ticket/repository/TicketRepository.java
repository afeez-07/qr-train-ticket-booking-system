package com.trainbooking.qr_train_ticket.repository;

import com.trainbooking.qr_train_ticket.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketRepository extends JpaRepository<Ticket,Long> {
}