package com.example.Movie_ticket_booking_service.service;

import com.example.Movie_ticket_booking_service.dto.request.CinemaRequest;
import com.example.Movie_ticket_booking_service.dto.response.*;
import com.example.Movie_ticket_booking_service.entity.CinemaTypeEntity;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface CinemaService{
    CinemaResponse createCinema(@Valid @RequestBody CinemaRequest cinemaRequest);
    CinemaResponse updateCinema(Long id, CinemaRequest cinemaRequest);
    List<CinemaSummaryResponse> getCinemas(String address);
    CinemaResponse getCinema(Long id);
    List<CinemaResponse> getListCinemas();
    void deleteCinema(Long id);
    List<CinemaTypeResponse> getCinemaTypes();

}
