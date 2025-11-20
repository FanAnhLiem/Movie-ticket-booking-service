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
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InvoiceDetailAD {
    private Long invoiceId;
    private String bookingCode;
    private String movieName;
    private String showTime;
    private String showDate;
    private String screenRoomTypeName;
    private String screenRoomName;
    private String cinema;
    private String bookDay;

    private Long userId;
    private String userName;
    private String userEmail;
    private String userPhone;
    private String status;
    private BigDecimal totalMoney;

    private List<SeatInvoice> seatList;

    @Data
    public static class SeatInvoice{
        private Long id;
        private String seatCode;
        private BigDecimal price;
    }
}


