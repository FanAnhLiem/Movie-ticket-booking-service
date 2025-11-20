package com.example.Movie_ticket_booking_service.service;

import com.example.Movie_ticket_booking_service.dto.SeatTypeResponse;
import com.example.Movie_ticket_booking_service.dto.request.SeatTypeRequest;

import java.util.List;

public interface SeatTypeService {
    SeatTypeResponse createSeatType(SeatTypeRequest seatTypeResponse);
    SeatTypeResponse updateSeatType(Long id, SeatTypeRequest seatTypeRequest);
    SeatTypeResponse getSeatType(Long id);
    List<SeatTypeResponse> getAllSeatType();
    boolean deleteSeatType(Long id);
}
