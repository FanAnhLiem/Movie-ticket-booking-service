package com.example.Movie_ticket_booking_service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class SeatTypeResponse {
    private Long id;
    private String name;
    private Double priceFactor;
}
