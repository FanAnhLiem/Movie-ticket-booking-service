package com.example.Movie_ticket_booking_service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InvoiceSummary {
    private Long id;
    private String code;
    private String movieName;
    private String showTime;
    private String screenRoom;
    private BigDecimal totalMoney;
    private String createDate;

}
