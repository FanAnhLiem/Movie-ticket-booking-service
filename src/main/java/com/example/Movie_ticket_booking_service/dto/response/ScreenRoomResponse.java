package com.example.Movie_ticket_booking_service.dto.response;

import lombok.Data;

@Data
public class ScreenRoomResponse {
    private Long id;
    private String name;
    private Long cinemaId;
    private Long screenRoomTypeId;
}
