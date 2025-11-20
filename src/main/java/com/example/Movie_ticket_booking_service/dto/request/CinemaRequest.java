package com.example.Movie_ticket_booking_service.dto.request;

import com.example.Movie_ticket_booking_service.dto.response.ScreenRoomResponse;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class CinemaRequest {
    @NotBlank(message = "Name is Required")
    @Size( max = 200, message = "Title must be between 3 and 200 characters")
    private String name;
    private Long cinemaTypeId;
    private String address;
}
