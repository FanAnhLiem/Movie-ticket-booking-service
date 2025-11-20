package com.example.Movie_ticket_booking_service.controller;

import com.example.Movie_ticket_booking_service.dto.request.AddressRequest;
import com.example.Movie_ticket_booking_service.dto.request.FilterCinema;
import com.example.Movie_ticket_booking_service.dto.response.ApiResponse;
import com.example.Movie_ticket_booking_service.dto.response.MovieShowTimeResponse;
import com.example.Movie_ticket_booking_service.dto.response.CinemaSummaryResponse;
import com.example.Movie_ticket_booking_service.service.CinemaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cinema")
@RequiredArgsConstructor
public class CinemaController {
    private final CinemaService cinemaService;

    @PostMapping("/address")
    ApiResponse<List<CinemaSummaryResponse>> getCinemas(@Valid @RequestBody AddressRequest addressRequest) {
        return ApiResponse.<List<CinemaSummaryResponse>>builder()
                .result(cinemaService.getCinemas(addressRequest.getAddress()))
                .build();
    }

}
