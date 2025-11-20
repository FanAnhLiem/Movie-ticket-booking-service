package com.example.Movie_ticket_booking_service.controller;

import com.example.Movie_ticket_booking_service.dto.response.ApiResponse;
import com.example.Movie_ticket_booking_service.dto.response.SeatShowTime;
import com.example.Movie_ticket_booking_service.dto.response.ShowTimeResponse;
import com.example.Movie_ticket_booking_service.entity.SeatEntity;
import com.example.Movie_ticket_booking_service.entity.ShowTimeEntity;
import com.example.Movie_ticket_booking_service.repository.SeatRepository;
import com.example.Movie_ticket_booking_service.repository.ShowTimeRepository;
import com.example.Movie_ticket_booking_service.service.SeatService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/seat")
@RequiredArgsConstructor
public class SeatController {
    private final SeatService seatService;
    private final SeatRepository seatRepository;
    private final ShowTimeRepository showTimeRepository;

    @GetMapping("/{showTimeId}")
    ApiResponse<SeatShowTime> getListSeat(@PathVariable("showTimeId") Long showTimeId){
        return ApiResponse.<SeatShowTime>builder()
                .result(seatService.getListSeat(showTimeId))
                .build();
    }


}
