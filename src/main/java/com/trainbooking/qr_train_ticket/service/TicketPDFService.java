package com.trainbooking.qr_train_ticket.service;

import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfWriter;
import com.trainbooking.qr_train_ticket.model.Ticket;
import com.trainbooking.qr_train_ticket.model.Train;
import com.trainbooking.qr_train_ticket.repository.TicketRepository;
import com.trainbooking.qr_train_ticket.repository.TrainRepository;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;

@Service
public class TicketPDFService {

    private final TicketRepository ticketRepo;
    private final TrainRepository trainRepo;
    private final TicketService ticketService;

    public TicketPDFService(
            TicketRepository ticketRepo,
            TrainRepository trainRepo,
            TicketService ticketService
    ) {
        this.ticketRepo = ticketRepo;
        this.trainRepo = trainRepo;
        this.ticketService = ticketService;
    }

    // ⭐ THIS METHOD IS WHAT CONTROLLER EXPECTS
    public byte[] generateTicketPDF(Long ticketId) {

        try {

            Ticket ticket = ticketRepo.findById(ticketId)
                    .orElseThrow(() -> new RuntimeException("Ticket not found"));

            Train train = trainRepo.findById(ticket.getTrainId())
                    .orElseThrow(() -> new RuntimeException("Train not found"));

            // Generate QR
            byte[] qrBytes = ticketService.generateQR(ticketId);

            Document document = new Document();
            ByteArrayOutputStream out = new ByteArrayOutputStream();

            PdfWriter.getInstance(document, out);
            document.open();

            document.add(new Paragraph("TRAIN TICKET"));
            document.add(new Paragraph("-----------------------------"));

            document.add(new Paragraph("Ticket ID : " + ticket.getId()));
            document.add(new Paragraph("Train ID : " + train.getId()));
            document.add(new Paragraph("Train Name : " + train.getTrainName()));
            document.add(new Paragraph("Source : " + train.getSource()));
            document.add(new Paragraph("Destination : " + train.getDestination()));
            document.add(new Paragraph("Seats Booked : " + ticket.getSeatsBooked()));
            document.add(new Paragraph("Total Fare : ₹ " + ticket.getTotalFare()));

            document.add(new Paragraph("\nQR Code:\n"));

            Image qrImage = Image.getInstance(qrBytes);
            qrImage.scaleAbsolute(150, 150);
            document.add(qrImage);

            document.close();

            return out.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}