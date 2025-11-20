package com.example.Movie_ticket_booking_service.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class ShowTimeRequest {

    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate showDate;

    @JsonFormat(pattern = "HH:mm")
    private LocalTime startTime;

    @JsonFormat(pattern = "HH:mm")
    private LocalTime endTime;

    @JsonProperty("screen_room_id")
    private Long screenRoomId;

    @JsonProperty("movie_id")
    private Long movieId;
}
