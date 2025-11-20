package com.example.Movie_ticket_booking_service.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class SeatTypeRequest {
    private String name;
    private Double priceFactor;
}
