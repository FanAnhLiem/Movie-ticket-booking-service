package com.example.Movie_ticket_booking_service.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShowTimeResponse {
    private Long id;

    private LocalTime startTime;

    private LocalTime endTime;

    private Long screenRoomId;

    private Long movieId;

    private LocalDate showDate;
}
