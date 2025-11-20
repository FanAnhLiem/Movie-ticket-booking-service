package com.example.Movie_ticket_booking_service.dto.request;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
public class testRq {
    private String name;
    private int duration;
    private String category;
    private String country;
    private String director;
    private String actors;
    private int ageLimit;
    private String trailer;

    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDate releaseDate;

    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDate endDate;
}
