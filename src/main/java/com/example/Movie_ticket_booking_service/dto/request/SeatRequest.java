package com.example.Movie_ticket_booking_service.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class SeatRequest {
    @JsonProperty("screen_room_id")
    private Long screenRoomId;

    @Min(value = 2, message = "number of row must be greater than or equal 2")
    @Max(value=26,message = "number of row must be less than or equal 36")
    private int rows;

    @Min(value = 2, message = "number of column must be greater than or equal 2")
    @Max(value=26,message = "number of column must be less than or equal 26")
    private int columns;

    private Long[][] seatTypeIds;
}
