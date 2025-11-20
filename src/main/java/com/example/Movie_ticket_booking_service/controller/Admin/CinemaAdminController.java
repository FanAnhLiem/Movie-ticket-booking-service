package com.example.Movie_ticket_booking_service.controller.Admin;

import com.example.Movie_ticket_booking_service.dto.request.CinemaRequest;
import com.example.Movie_ticket_booking_service.dto.response.*;
import com.example.Movie_ticket_booking_service.entity.CinemaTypeEntity;
import com.example.Movie_ticket_booking_service.service.CinemaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/cinema")
@RequiredArgsConstructor
public class CinemaAdminController {
    private final CinemaService cinemaService;

    @PostMapping
    public ApiResponse<CinemaResponse> createCinema(@Valid @RequestBody CinemaRequest cinemaRequest) {
        return ApiResponse.<CinemaResponse>builder()
                .result(cinemaService.createCinema(cinemaRequest))
                .build();
    }

    @GetMapping("/type")
    public ApiResponse<List<CinemaTypeResponse>> getCinemaTypes() {
        return ApiResponse.<List<CinemaTypeResponse>>builder()
                .result(cinemaService.getCinemaTypes())
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<CinemaResponse> getCinema(@Valid @PathVariable Long id) {
        return ApiResponse.<CinemaResponse>builder()
                .result(cinemaService.getCinema(id))
                .build();
    }

    @GetMapping()
    public ApiResponse<List<CinemaResponse>> getCinema() {
        return ApiResponse.<List<CinemaResponse>>builder()
                .result(cinemaService.getListCinemas())
                .build();
    }

    @PutMapping("/{id}")
    public ApiResponse<CinemaResponse> updateCinema(@Valid @PathVariable Long id,
                                                                   @RequestBody CinemaRequest cinemaRequest) {
        return ApiResponse.<CinemaResponse>builder()
                .result(cinemaService.updateCinema(id, cinemaRequest))
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<String> deleteCinema(@Valid @PathVariable Long id) {
        cinemaService.deleteCinema(id);
        return ApiResponse.<String>builder()
                .message("Movie delete successfully")
                .build();
    }
}
