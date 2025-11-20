package com.example.Movie_ticket_booking_service.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
@Data
@Builder
public class ShowTimeDTO {
    private Long id;
    private String movieName;
    private LocalDate showDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private Long screenRoomId;
}
