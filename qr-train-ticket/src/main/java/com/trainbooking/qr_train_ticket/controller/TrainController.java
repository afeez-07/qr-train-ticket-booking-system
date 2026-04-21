package com.trainbooking.qr_train_ticket.controller;

import com.trainbooking.qr_train_ticket.model.Train;
import com.trainbooking.qr_train_ticket.repository.TrainRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/trains")
@CrossOrigin
public class TrainController {

    private final TrainRepository repo;

    public TrainController(TrainRepository repo){
        this.repo=repo;
    }

    @GetMapping("/search")
    public List<Train> search(
            @RequestParam String source,
            @RequestParam String destination){

        return repo.findBySourceIgnoreCaseAndDestinationIgnoreCase(source,destination);
    }
}