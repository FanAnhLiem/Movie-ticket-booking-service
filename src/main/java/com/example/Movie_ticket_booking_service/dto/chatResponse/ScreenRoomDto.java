package com.example.Movie_ticket_booking_service.dto.chatResponse;

public record ScreenRoomDto(String name,
                            String cinemaName,
                            String roomType,
                            Integer seatCount,
                            String  active) {
}
