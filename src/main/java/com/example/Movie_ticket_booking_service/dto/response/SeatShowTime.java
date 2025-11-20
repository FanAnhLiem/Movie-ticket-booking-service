package com.example.Movie_ticket_booking_service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SeatShowTime {
    private Long showTimeId;
    private String movieName;
    private String cinemaName;
    private int sumSeats;
    private List<SeatShowTimeResponse> seatList;
}
