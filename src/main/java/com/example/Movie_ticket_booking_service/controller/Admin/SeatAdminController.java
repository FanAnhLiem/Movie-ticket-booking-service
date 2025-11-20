package com.example.Movie_ticket_booking_service.controller.Admin;

import com.example.Movie_ticket_booking_service.dto.request.SeatRequest;
import com.example.Movie_ticket_booking_service.dto.response.ApiResponse;
import com.example.Movie_ticket_booking_service.dto.response.SeatResponse;
import com.example.Movie_ticket_booking_service.dto.response.SeatSummaryRepo;
import com.example.Movie_ticket_booking_service.service.SeatService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/seat")
@RequiredArgsConstructor
public class SeatAdminController {
    private final SeatService seatService;

    @PostMapping("/layout")
    public ApiResponse<List<SeatResponse>> createSeats(@RequestBody SeatRequest seatRequest){
        return ApiResponse.<List<SeatResponse>>builder()
                .result(seatService.createSeats(seatRequest))
                .build();
    }

    @GetMapping("/screenroom/{screenRoomId}")
    public ApiResponse<List<SeatSummaryRepo>> getSeatListByScreenRoom(@PathVariable Long screenRoomId){
        return ApiResponse.<List<SeatSummaryRepo>>builder()
                .result(seatService.getSeatListByScreenRoom(screenRoomId))
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteSeat(@PathVariable Long id){
        seatService.deleteSeat(id);
        return ApiResponse.<Void>builder()
                .message("Seat has been deleted")
                .build();
    }

}
