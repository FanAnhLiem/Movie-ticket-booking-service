package com.example.Movie_ticket_booking_service.dto.chatResponse;

public record ShowTimeDto(
        Long id,
        String movieName,
        String cinemaName,
        String screenName,
        String showDate,
        String startTime,
        String endTime
) {
}
