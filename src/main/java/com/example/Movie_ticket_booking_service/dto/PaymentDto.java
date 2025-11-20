package com.example.Movie_ticket_booking_service.dto;

import lombok.Data;

@Data
public class PaymentDto {
    private Long amount;
    private String bankCode;
}
