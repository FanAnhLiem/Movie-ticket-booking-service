package com.example.Movie_ticket_booking_service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MovieDetailResponse {
    private Long id;
    private String description;
    private int duration;
    private String category;
    private String country;
    private String director;
    private String actors;
    private String posterUrl;
    private int ageLimit;
    private String trailer;
    private boolean status;
    private String releaseDate;
    private String endDate;
    private String createAt;
}
