package com.trainbooking.qr_train_ticket.repository;

import com.trainbooking.qr_train_ticket.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByUsername(String username);
}