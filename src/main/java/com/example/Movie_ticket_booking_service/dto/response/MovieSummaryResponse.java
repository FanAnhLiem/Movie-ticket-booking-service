package com.example.Movie_ticket_booking_service.dto.response;

import lombok.Data;

import java.time.LocalDate;
@Data
public class MovieSummaryResponse {
    private Long id;
    private String name;
    private int duration;
    private String posterUrl;
    private String category;
    private LocalDate releaseDate;
}
