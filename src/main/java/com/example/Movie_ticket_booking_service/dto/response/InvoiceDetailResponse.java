package com.example.Movie_ticket_booking_service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class InvoiceDetailResponse {
    private Long invoiceId;
    private String movieName;
    private String screenRoomType;
    private String bookingCode;
    private LocalDate showDate;
    private LocalTime startTime;
    private String screenRoomName;
    private List<String> seatList;
    private int totalTicket;
    private BigDecimal totalMoney;
    private Long userId;
    private String userName;
}
