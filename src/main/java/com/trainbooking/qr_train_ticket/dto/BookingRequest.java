package com.trainbooking.qr_train_ticket.dto;

public class BookingRequest {

    private Long trainId;
    private int seats;

    public BookingRequest() {}

    public Long getTrainId() {
        return trainId;
    }

    public void setTrainId(Long trainId) {
        this.trainId = trainId;
    }

    public int getSeats() {
        return seats;
    }

    public void setSeats(int seats) {
        this.seats = seats;
    }
}