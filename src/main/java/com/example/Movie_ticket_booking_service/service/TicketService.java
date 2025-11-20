package com.example.Movie_ticket_booking_service.service;

import com.example.Movie_ticket_booking_service.entity.InvoiceEntity;
import com.example.Movie_ticket_booking_service.entity.TicketEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketService{
    void createTicket(String vnp_TxRef, InvoiceEntity invoiceEntity);
}
