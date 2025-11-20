package com.example.Movie_ticket_booking_service.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ScreenRoomDetailResponse {
    private Long id;
    private String name;
    private String roomType;
    private String status;
}
