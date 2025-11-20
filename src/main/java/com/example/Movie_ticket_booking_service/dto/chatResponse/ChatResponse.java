package com.example.Movie_ticket_booking_service.dto.chatResponse;

import com.example.Movie_ticket_booking_service.repository.ScreenRoomRepository;

import java.util.List;

public record ChatResponse(
        String type,
        String message,

        MovieDto movie,
        CinemaDto cinema,
        ScreenRoomDto screen,

        List<MovieDto> movies,
        List<CinemaDto> cinemas,
        List<ScreenRoomDto> screens,
        List<ScreenRoomTypeDto> types,
        List<ShowTimeDto> showtimes
) {
}
