package com.trainbooking.qr_train_ticket.repository;

import com.trainbooking.qr_train_ticket.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface TicketRepository extends JpaRepository<Ticket,Long> {

    boolean existsByTicketNumber(String ticketNumber);

    Optional<Ticket> findByTicketNumber(String ticketNumber);

}