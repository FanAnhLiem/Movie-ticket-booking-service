package com.example.Movie_ticket_booking_service.dto.response;

import jakarta.persistence.Column;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SeatResponse {
    private Long id;
    private Long screenRoomId;
    private Long seatTypeId;
    private String seatCode;
    private int row;
    private int column;
}
