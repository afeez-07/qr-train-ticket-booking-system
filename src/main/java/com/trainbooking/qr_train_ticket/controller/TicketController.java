package com.trainbooking.qr_train_ticket.controller;

import com.trainbooking.qr_train_ticket.dto.BookingRequest;
import com.trainbooking.qr_train_ticket.model.Ticket;
import com.trainbooking.qr_train_ticket.service.TicketPDFService;
import com.trainbooking.qr_train_ticket.service.TicketService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ticket")
@CrossOrigin("*")
public class TicketController {

    private final TicketService ticketService;
    private final TicketPDFService pdfService;

    public TicketController(TicketService ticketService,
                            TicketPDFService pdfService) {
        this.ticketService = ticketService;
        this.pdfService = pdfService;
    }

    /* ================= BOOK TICKET ================= */
    @PostMapping("/book")
    public Ticket bookTicket(@RequestBody BookingRequest request) {
        return ticketService.bookTicket(
                request.getTrainId(),
                request.getSeats()
        );
    }

    /* ================= QR CODE ================= */
    @GetMapping("/qr/{ticketId}")
    public ResponseEntity<byte[]> getQR(@PathVariable Long ticketId) {

        byte[] qr = ticketService.generateQR(ticketId);

        return ResponseEntity
                .ok()
                .header("Content-Type", "image/png")
                .body(qr);
    }

    /* ================= PDF DOWNLOAD ================= */
    @GetMapping("/pdf/{ticketId}")
    public ResponseEntity<byte[]> downloadPDF(@PathVariable Long ticketId) {

        byte[] pdf = pdfService.generateTicketPDF(ticketId);

        return ResponseEntity
                .ok()
                .header("Content-Type", "application/pdf")
                .body(pdf);
    }

    /* ================= TICKET CANCEL ================= */
    @DeleteMapping("/cancel/{ticketId}")
    public String cancelTicket(@PathVariable Long ticketId) {

        ticketService.cancelTicket(ticketId);
        return "Ticket Cancelled Successfully";
    }
}