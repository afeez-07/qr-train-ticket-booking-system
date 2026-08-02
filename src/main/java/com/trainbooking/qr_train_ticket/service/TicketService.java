package com.trainbooking.qr_train_ticket.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.trainbooking.qr_train_ticket.model.Ticket;
import com.trainbooking.qr_train_ticket.model.Train;
import com.trainbooking.qr_train_ticket.repository.TicketRepository;
import com.trainbooking.qr_train_ticket.repository.TrainRepository;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.time.LocalDateTime;

@Service
public class TicketService {

    private final TicketRepository ticketRepo;
    private final TrainRepository trainRepo;

    private final Random random = new Random();

    public TicketService(TicketRepository ticketRepo, TrainRepository trainRepo) {
        this.ticketRepo = ticketRepo;
        this.trainRepo = trainRepo;
    }

    // ⭐ BOOK TICKET
    public Map<String, Object> bookTicket(Long trainId, int seats, String passengerName, int passengerAge) {

        Train train = trainRepo.findById(trainId)
                .orElseThrow(() -> new RuntimeException("Train not found"));

        if (train.getAvailableSeats() < seats) {
            throw new RuntimeException("Not enough seats");
        }

        train.setAvailableSeats(train.getAvailableSeats() - seats);
        trainRepo.save(train);

        Ticket ticket = new Ticket();
        ticket.setTrainId(trainId);
        ticket.setPassengerName(passengerName);
        ticket.setPassengerAge(passengerAge);
        ticket.setSeatsBooked(seats);
        ticket.setTotalFare(seats * train.getFare());
        ticket.setTicketNumber(generateTicketNumber());
        ticket.setBookingDateTime(LocalDateTime.now());

        Ticket savedTicket = ticketRepo.save(ticket);

        // ✅ Create custom response
        Map<String, Object> response = new HashMap<>();
        response.put("id", savedTicket.getId());
        response.put("ticketNumber", savedTicket.getTicketNumber());
        response.put("passengerName", savedTicket.getPassengerName());
        response.put("totalFare", savedTicket.getTotalFare());
        response.put("remainingSeats", train.getAvailableSeats());

        return response;
    }
    // ⭐ GENERATE QR
    public byte[] generateQR(Long ticketId) {
        try {

            Ticket t = ticketRepo.findById(ticketId).orElseThrow();

            Train train = trainRepo.findById(t.getTrainId()).orElseThrow();

            String text =
                    "QR Train Ticket\n" +
                            "----------------------\n" +
                            "Ticket No : " + t.getTicketNumber() + "\n" +
                            "Train No : " + train.getTrainNumber() + "\n" +
                            "Passenger : " + t.getPassengerName() + "\n" +
                            "Passenger Age : " + t.getPassengerAge() + "\n" +
                            "Train Name : " + train.getTrainName() + "\n" +
                            "Source : " + train.getSource() + "\n" +
                            "Destination : " + train.getDestination() + "\n" +
                            "Seats Booked : " + t.getSeatsBooked() + "\n" +
                            "Total Fare : Rs. " + (int) t.getTotalFare();

            QRCodeWriter writer = new QRCodeWriter();
            BitMatrix matrix = writer.encode(text, BarcodeFormat.QR_CODE, 200, 200);

            BufferedImage image = new BufferedImage(200, 200, BufferedImage.TYPE_INT_RGB);

            for (int x = 0; x < 200; x++) {
                for (int y = 0; y < 200; y++) {
                    image.setRGB(x, y, matrix.get(x, y) ? 0 : 16777215);
                }
            }

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ImageIO.write(image, "PNG", out);

            return out.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // GENERATE TICKET NUMBER
    private String generateTicketNumber() {

        String ticketNumber;

        do {
            int number = 1000 + random.nextInt(9000);
            ticketNumber = "TKT" + number;
        } while (ticketRepo.existsByTicketNumber(ticketNumber));

        return ticketNumber;
    }

    // CANCEL TICKET
    public void cancelTicket(String ticketNumber) {

        Ticket ticket = ticketRepo.findByTicketNumber(ticketNumber)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));

        Train train = trainRepo.findById(ticket.getTrainId())
                .orElseThrow(() -> new RuntimeException("Train not found"));

        // Return seats back
        train.setAvailableSeats(
                train.getAvailableSeats() + ticket.getSeatsBooked()
        );

        trainRepo.save(train);

        ticketRepo.delete(ticket);
    }
}