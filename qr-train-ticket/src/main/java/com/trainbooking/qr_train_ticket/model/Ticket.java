package com.trainbooking.qr_train_ticket.model;

import jakarta.persistence.*;

@Entity
@Table(name = "tickets")
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long trainId;
    private int seatsBooked;
    private double totalFare;

    private String passengerName;
    private int passengerAge;

    // ===== GETTERS =====

    public Long getId() {
        return id;
    }

    public Long getTrainId() {
        return trainId;
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

    // ===== SETTERS =====

    public void setTrainId(Long trainId) {
        this.trainId = trainId;
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
}