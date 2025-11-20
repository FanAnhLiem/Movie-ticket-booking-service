package com.example.Movie_ticket_booking_service.service;

import com.example.Movie_ticket_booking_service.dto.request.ScreenRoomTypeRequest;
import com.example.Movie_ticket_booking_service.dto.response.ScreenRoomTypeResponse;

import java.util.List;

public interface ScreenRoomTypeService {
    ScreenRoomTypeResponse createScreenRoomType(ScreenRoomTypeRequest screenRoomTypeRequest);
    ScreenRoomTypeResponse updateScreenRoomType(Long id, ScreenRoomTypeRequest screenRoomTypeRequest);
    ScreenRoomTypeResponse getScreenRoomType(Long id);
    List<ScreenRoomTypeResponse> getAllScreenRoomType();
    boolean deleteScreenRoomType(Long id);
}
