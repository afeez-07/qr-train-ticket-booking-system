package com.trainbooking.qr_train_ticket.controller;

import com.trainbooking.qr_train_ticket.model.Ticket;
import com.trainbooking.qr_train_ticket.model.Train;
import com.trainbooking.qr_train_ticket.repository.TicketRepository;
import com.trainbooking.qr_train_ticket.repository.TrainRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@CrossOrigin("*")
public class AdminController {

    private final TrainRepository trainRepo;
    private final TicketRepository ticketRepo;

    public AdminController(TrainRepository trainRepo,
                           TicketRepository ticketRepo) {
        this.trainRepo = trainRepo;
        this.ticketRepo = ticketRepo;
    }

    // =========================
    // ADMIN LOGIN
    // =========================
    @PostMapping("/login")
    public ResponseEntity<String> adminLogin(@RequestBody AdminRequest request) {

        if ("admin".equals(request.getUsername()) &&
                "admin123".equals(request.getPassword())) {

            return ResponseEntity.ok("Admin Login Success");
        }

        return ResponseEntity.status(401).body("Invalid Admin Credentials");
    }

    // =========================
    // ADD TRAIN
    // =========================
    @PostMapping("/add-train")
    public Train addTrain(@RequestBody Train train) {
        return trainRepo.save(train);
    }

    // =========================
    // VIEW ALL TRAINS
    // =========================
    @GetMapping("/trains")
    public List<Train> getAllTrains() {
        return trainRepo.findAll();
    }

    // =========================
    // DELETE TRAIN
    // =========================
    @DeleteMapping("/delete-train/{id}")
    public ResponseEntity<String> deleteTrain(@PathVariable Long id) {

        if (!trainRepo.existsById(id)) {
            return ResponseEntity.badRequest().body("Train not found");
        }

        trainRepo.deleteById(id);
        return ResponseEntity.ok("Train deleted successfully");
    }

    // =========================
    // VIEW ALL BOOKINGS
    // =========================
    @GetMapping("/tickets")
    public List<Ticket> getAllTickets() {
        return ticketRepo.findAll();
    }

    // =========================
    // ADMIN REQUEST DTO (INNER CLASS)
    // =========================
    static class AdminRequest {

        private String username;
        private String password;

        public String getUsername() { return username; }
        public String getPassword() { return password; }

        public void setUsername(String username) { this.username = username; }
        public void setPassword(String password) { this.password = password; }
    }
}