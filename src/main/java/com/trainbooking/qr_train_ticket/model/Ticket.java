package com.trainbooking.qr_train_ticket.model;

import jakarta.persistence.*;

@Entity
@Table(name="tickets")
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long trainId;
    private int seatsBooked;
    private double totalFare;

    public Long getId(){ return id; }

    public Long getTrainId(){ return trainId; }
    public void setTrainId(Long trainId){ this.trainId=trainId; }

    public int getSeatsBooked(){ return seatsBooked; }
    public void setSeatsBooked(int seatsBooked){ this.seatsBooked=seatsBooked; }

    public double getTotalFare(){ return totalFare; }
    public void setTotalFare(double totalFare){ this.totalFare=totalFare; }
}