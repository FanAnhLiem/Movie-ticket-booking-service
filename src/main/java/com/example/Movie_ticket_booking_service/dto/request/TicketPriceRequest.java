package com.example.Movie_ticket_booking_service.dto.request;

import lombok.Data;

@Data
public class TicketPriceRequest {
    private Long cinemaTypeId;
    private Long screenRoomTypeId;
    private Long seatTypeId;
    private String timeFrame;
    private String dayType;
}
