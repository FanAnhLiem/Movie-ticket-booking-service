package com.example.Movie_ticket_booking_service.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SeatSummaryRepo {
    private Long id;
    private String seatTypeName;
    private Long seatTypeId;
    private String seatCode;
}
