package com.example.Movie_ticket_booking_service.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScreenRoomTypeRequest {
    private String name;
    private Double priceFactor;
}
