package com.trainbooking.qr_train_ticket.repository;

import com.trainbooking.qr_train_ticket.model.Train;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TrainRepository extends JpaRepository<Train,Long> {

    List<Train> findBySourceIgnoreCaseAndDestinationIgnoreCase(String source,String destination);
}