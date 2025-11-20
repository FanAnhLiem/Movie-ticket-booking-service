package com.example.Movie_ticket_booking_service.service;

import com.example.Movie_ticket_booking_service.dto.request.ScreenRoomRequest;
import com.example.Movie_ticket_booking_service.dto.response.ScreenRoomDetailResponse;
import com.example.Movie_ticket_booking_service.dto.response.ScreenRoomResponse;
import com.example.Movie_ticket_booking_service.entity.ScreenRoomEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScreenRoomService{
    ScreenRoomResponse createScreenRoom(ScreenRoomRequest screenRoomRequest);
    ScreenRoomResponse updateScreenRoom(Long id, ScreenRoomRequest screenRoomRequest);
    List<ScreenRoomDetailResponse> getScreenRoomList(Long id);
    ScreenRoomResponse getScreenRoomDetail(Long id);
    boolean deleteScreenRoom(Long id);
    List<ScreenRoomDetailResponse> getScreenRoomActiveList(Long id);
}
