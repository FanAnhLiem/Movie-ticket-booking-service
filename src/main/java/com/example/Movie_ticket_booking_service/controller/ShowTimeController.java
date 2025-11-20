package com.example.Movie_ticket_booking_service.controller;

import com.example.Movie_ticket_booking_service.dto.request.FilterCinema;
import com.example.Movie_ticket_booking_service.dto.request.FilterMovie;
import com.example.Movie_ticket_booking_service.dto.response.*;
import com.example.Movie_ticket_booking_service.service.ShowTimeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/showtime")
@RequiredArgsConstructor
public class ShowTimeController {
    private final ShowTimeService showTimeService;

    @PostMapping("/cinema")
    ApiResponse<List<MovieShowTimeResponse>> getListShowTimeFilterCinema(@RequestBody FilterCinema filterCinema) {
        return ApiResponse.<List<MovieShowTimeResponse>>builder()
                .result(showTimeService.getListShowTimeFilterCinema(filterCinema))
                .build();
    }

    @PostMapping("/movie")
    ApiResponse<List<CinemaShowTimeResponse>> getListShowTimeFilterMovie(@RequestBody FilterMovie filterMovie) {
        return ApiResponse.<List<CinemaShowTimeResponse>>builder()
                .result(showTimeService.getListShowTimeFilterMovie(filterMovie))
                .build();
    }
}
