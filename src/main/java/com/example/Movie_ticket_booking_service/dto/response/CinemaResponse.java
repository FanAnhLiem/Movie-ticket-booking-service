package com.example.Movie_ticket_booking_service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CinemaResponse {
    private Long id;
    private String name;
    private String address;
    private String cinemaTypeName;
    private String status;
}
