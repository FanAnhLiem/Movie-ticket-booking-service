package com.example.Movie_ticket_booking_service.dto.request;

import com.example.Movie_ticket_booking_service.entity.CinemaEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScreenRoomRequest {
    private String name;

    @JsonProperty("cinema_id")
    private Long cinemaId;

    @JsonProperty("screen_room_type_id")
    private Long screenRoomTypeId;
}
