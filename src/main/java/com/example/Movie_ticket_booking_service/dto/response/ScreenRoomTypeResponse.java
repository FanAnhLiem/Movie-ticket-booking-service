package com.example.Movie_ticket_booking_service.dto.response;

import lombok.Data;

@Data
public class ScreenRoomTypeResponse {
    private Long id;
    private String name;
    private Double priceFactor;
}
