package com.trainbooking.qr_train_ticket.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "tickets")
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String ticketNumber;
    private Long trainId;
    private int seatsBooked;
    private double totalFare;

    private String passengerName;
    private int passengerAge;
    private LocalDateTime bookingDateTime;

    // ===== GETTERS =====

    public Long getId() {
        return id;
    }

    public Long getTrainId() {
        return trainId;
    }

    public String getTicketNumber() {
        return ticketNumber;
    }

    public int getSeatsBooked() {
        return seatsBooked;
    }

    public double getTotalFare() {
        return totalFare;
    }

    public String getPassengerName() {
        return passengerName;
    }

    public int getPassengerAge() {
        return passengerAge;
    }

    public LocalDateTime getBookingDateTime() { return bookingDateTime; }

    // ===== SETTERS =====

    public void setTrainId(Long trainId) {
        this.trainId = trainId;
    }

    public void setTicketNumber(String ticketNumber) {
        this.ticketNumber = ticketNumber;
    }

    public void setSeatsBooked(int seatsBooked) {
        this.seatsBooked = seatsBooked;
    }

    public void setTotalFare(double totalFare) {
        this.totalFare = totalFare;
    }

    public void setPassengerName(String passengerName) {
        this.passengerName = passengerName;
    }

    public void setPassengerAge(int passengerAge) {
        this.passengerAge = passengerAge;
    }

    public void setBookingDateTime(LocalDateTime bookingDateTime) { this.bookingDateTime = bookingDateTime; }
}