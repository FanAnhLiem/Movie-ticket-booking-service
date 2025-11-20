package com.example.Movie_ticket_booking_service.service;

import com.example.Movie_ticket_booking_service.dto.request.InvoiceRequest;
import com.example.Movie_ticket_booking_service.dto.response.InvoiceDetailAD;
import com.example.Movie_ticket_booking_service.dto.response.InvoiceDetailResponse;
import com.example.Movie_ticket_booking_service.dto.response.InvoiceResponse;
import com.example.Movie_ticket_booking_service.dto.response.InvoiceSummary;
import com.example.Movie_ticket_booking_service.entity.InvoiceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface InvoiceService{
    void createInvoice(InvoiceRequest invoiceRequest);
    void updateInvoice(String vnp_TxnRef);
    List<InvoiceResponse> getInvoiceList();
    InvoiceDetailResponse getInvoice(Long invoiceId);
    InvoiceDetailResponse getInvoiceByBookingCode(String bookingCode);
    void checkInInvoice(Long invoiceId);
    List<InvoiceSummary> getInvoiceSummaryList();
    InvoiceDetailAD getInvoiceDetail(Long id);
}
