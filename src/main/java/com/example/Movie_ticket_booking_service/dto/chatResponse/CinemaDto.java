package com.example.Movie_ticket_booking_service.dto.chatResponse;

public record CinemaDto(String name,
                        String cinemaType,
                        String address,
                        String active,
                        Integer numberOfScreens) {
}
