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

@Service
public class TicketService {

    private final TicketRepository ticketRepo;
    private final TrainRepository trainRepo;

    public TicketService(TicketRepository ticketRepo, TrainRepository trainRepo) {
        this.ticketRepo = ticketRepo;
        this.trainRepo = trainRepo;
    }

    // ⭐ BOOK TICKET
    public Ticket bookTicket(Long trainId, int seats) {

        Train train = trainRepo.findById(trainId)
                .orElseThrow(() -> new RuntimeException("Train not found"));

        if (train.getAvailableSeats() < seats) {
            throw new RuntimeException("Not enough seats");
        }

        train.setAvailableSeats(train.getAvailableSeats() - seats);
        trainRepo.save(train);

        Ticket ticket = new Ticket();
        ticket.setTrainId(trainId);
        ticket.setSeatsBooked(seats);
        ticket.setTotalFare(seats * train.getFare());

        return ticketRepo.save(ticket);
    }

    // ⭐ GENERATE QR
    public byte[] generateQR(Long ticketId) {
        try {

            Ticket t = ticketRepo.findById(ticketId).orElseThrow();

            String text =
                    "Ticket ID: " + t.getId() +
                            "\nTrain ID: " + t.getTrainId() +
                            "\nSeats: " + t.getSeatsBooked() +
                            "\nFare: " + t.getTotalFare();

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

    // ⭐ CANCEL TICKET
    public void cancelTicket(Long ticketId) {

        Ticket ticket = ticketRepo.findById(ticketId)
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