package com.example.Movie_ticket_booking_service.service;

import com.example.Movie_ticket_booking_service.dto.request.SeatRequest;
import com.example.Movie_ticket_booking_service.dto.response.SeatResponse;
import com.example.Movie_ticket_booking_service.dto.response.SeatShowTime;
import com.example.Movie_ticket_booking_service.dto.response.SeatSummaryRepo;

import java.util.List;

public interface SeatService{
    List<SeatResponse> createSeats(SeatRequest seatRequest);
    SeatShowTime getListSeat(Long showTimeId);
    List<SeatSummaryRepo> getSeatListByScreenRoom(long showRoomId);
    void deleteSeat(Long id);

}
