package com.example.Movie_ticket_booking_service.controller;

import com.example.Movie_ticket_booking_service.dto.request.FilterMovie;
import com.example.Movie_ticket_booking_service.dto.response.ApiResponse;
import com.example.Movie_ticket_booking_service.dto.response.MovieDetailResponse;
import com.example.Movie_ticket_booking_service.dto.response.MovieSummaryResponse;
import com.example.Movie_ticket_booking_service.service.MovieService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/movie")
@RequiredArgsConstructor
public class MovieController {
    private final MovieService movieService;

    @GetMapping()
    public ApiResponse<List<MovieSummaryResponse>> getMovies() {
        return ApiResponse.<List<MovieSummaryResponse>>builder()
                .result(movieService.getAllMovies())
                .build();
    }

    @GetMapping("/nowplaying")
    public ApiResponse<List<MovieSummaryResponse>> getNowPlayingMovies() {
        return ApiResponse.<List<MovieSummaryResponse>>builder()
                .result(movieService.getNowPlayingMovies())
                .build();
    }

    @GetMapping("/upcoming")
    public ApiResponse<List<MovieSummaryResponse>> getUpcomingMovies() {
        return ApiResponse.<List<MovieSummaryResponse>>builder()
                .result(movieService.getUpcomingMovies())
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<MovieDetailResponse> getMovie(@Valid @PathVariable Long id) {
        return ApiResponse.<MovieDetailResponse>builder()
                .result(movieService.getMovie(id))
                .build();
    }


    @PostMapping("/address")
    public ApiResponse<List<String>> getListCinemaAddress(@RequestBody FilterMovie filterMovie) {
        return ApiResponse.<List<String>>builder()
                .result(movieService.getListCinemaAddress(filterMovie))
                .build();
    }
}
