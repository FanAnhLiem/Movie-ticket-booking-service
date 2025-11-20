package com.example.Movie_ticket_booking_service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class InvoiceResponse {
    private Long invoiceId;
    private String movieName;
    private int totalTicket;
    private LocalDate showDate;
    private LocalTime startTime;
    private BigDecimal totalMoney;
}
