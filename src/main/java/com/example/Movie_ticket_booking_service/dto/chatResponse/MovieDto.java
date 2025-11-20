package com.example.Movie_ticket_booking_service.dto.chatResponse;

public record MovieDto(String name,
                       String description,
                       String category,
                       String country,
                       Integer ageLimit,
                       String duration,
                       String director,
                       String actors,
                       String releaseDate,
                       String posterUrl,
                       String active) {
}
