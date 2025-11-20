package com.example.Movie_ticket_booking_service.dto.response;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class CheckOutResponse {
    private String status;
    private String paymentUrl;
}
