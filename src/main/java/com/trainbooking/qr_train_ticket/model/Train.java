package com.trainbooking.qr_train_ticket.model;

import jakarta.persistence.*;

@Entity
@Table(name="trains")
public class Train {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String trainName;
    private String source;
    private String destination;
    private String departureTime;
    private String arrivalTime;
    private int fare;

    @Column(name = "available_seats")   // ⭐ ADD THIS
    private int availableSeats;

    public Long getId(){ return id; }

    public String getTrainName(){ return trainName; }
    public void setTrainName(String trainName){ this.trainName=trainName; }

    public String getSource(){ return source; }
    public void setSource(String source){ this.source=source; }

    public String getDestination(){ return destination; }
    public void setDestination(String destination){ this.destination=destination; }

    public String getDepartureTime(){ return departureTime; }
    public void setDepartureTime(String departureTime){ this.departureTime=departureTime; }

    public String getArrivalTime(){ return arrivalTime; }
    public void setArrivalTime(String arrivalTime){ this.arrivalTime=arrivalTime; }

    public int getFare(){ return fare; }
    public void setFare(int fare){ this.fare=fare; }

    public int getAvailableSeats(){ return availableSeats; }
    public void setAvailableSeats(int availableSeats){ this.availableSeats=availableSeats; }
}