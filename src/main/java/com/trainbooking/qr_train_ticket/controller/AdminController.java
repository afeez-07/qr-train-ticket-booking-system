package com.trainbooking.qr_train_ticket.controller;

import com.trainbooking.qr_train_ticket.model.Ticket;
import com.trainbooking.qr_train_ticket.model.Train;
import com.trainbooking.qr_train_ticket.service.TicketService;
import com.trainbooking.qr_train_ticket.repository.TicketRepository;
import com.trainbooking.qr_train_ticket.repository.TrainRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;

@RestController
@RequestMapping("/admin")
@CrossOrigin("*")
public class AdminController {

    private final TrainRepository trainRepo;
    private final TicketRepository ticketRepo;
    private final TicketService ticketService;

    public AdminController(TrainRepository trainRepo,
                           TicketRepository ticketRepo,
                           TicketService ticketService) {
        this.trainRepo = trainRepo;
        this.ticketRepo = ticketRepo;
        this.ticketService = ticketService;
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
    public ResponseEntity<?> addTrain(@RequestBody Train train) {

        if (train.getTrainNumber() == null || train.getTrainNumber().trim().isEmpty()
                || train.getTrainName() == null || train.getTrainName().trim().isEmpty()
                || train.getSource() == null || train.getSource().trim().isEmpty()
                || train.getDestination() == null || train.getDestination().trim().isEmpty()
                || train.getDepartureTime() == null || train.getDepartureTime().trim().isEmpty()
                || train.getArrivalTime() == null || train.getArrivalTime().trim().isEmpty()
                || train.getFare() <= 0
                || train.getAvailableSeats() <= 0) {

            return ResponseEntity.badRequest().body("Please fill all fields correctly.");
        }

        return ResponseEntity.ok(trainRepo.save(train));
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
    // DELETE TICKET
    // =========================

    @DeleteMapping("/delete-ticket/{ticketNumber}")
    public ResponseEntity<String> deleteTicket(
            @PathVariable String ticketNumber){

        ticketService.deleteTicket(ticketNumber);

        return ResponseEntity.ok("Ticket deleted successfully");
    }

    // =========================
    // VIEW ALL BOOKINGS
    // =========================
    @GetMapping("/tickets")
    public List<Map<String,Object>> getAllTickets(){

        List<Ticket> tickets = ticketRepo.findAll();
        List<Map<String,Object>> result = new ArrayList<>();

        for(Ticket t : tickets){

            Train train = trainRepo.findById(t.getTrainId()).orElse(null);

            Map<String,Object> map = new HashMap<>();

            map.put("id", t.getId());
            map.put("ticketNumber", t.getTicketNumber());
            map.put("passengerName", t.getPassengerName());
            map.put("passengerAge", t.getPassengerAge());
            map.put("trainName", train != null ? train.getTrainName() : "Unknown");
            map.put("trainNumber", train != null ? train.getTrainNumber() : "Undefined");
            map.put("seatsBooked", t.getSeatsBooked());
            map.put("totalFare", t.getTotalFare());

            result.add(map);
        }

        return result;
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