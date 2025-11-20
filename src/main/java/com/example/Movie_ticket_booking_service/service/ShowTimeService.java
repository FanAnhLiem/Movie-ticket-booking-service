package com.example.Movie_ticket_booking_service.service;

import com.example.Movie_ticket_booking_service.dto.chatResponse.ShowTimeDto;
import com.example.Movie_ticket_booking_service.dto.request.FilterCinema;
import com.example.Movie_ticket_booking_service.dto.request.FilterMovie;
import com.example.Movie_ticket_booking_service.dto.request.ShowTimeRequest;
import com.example.Movie_ticket_booking_service.dto.response.*;

import java.time.LocalDate;
import java.util.List;

public interface ShowTimeService {
    ShowTimeResponse createShowTime(ShowTimeRequest showTimeRequest);
    ShowTimeResponse updateShowTime(ShowTimeRequest showTimeRequest, Long id);
    ShowTimeResponse getShowTime(Long id);
    List<ShowTimeDTO> getShowTimeList(Long screenRoomId, LocalDate date);
    List<MovieShowTimeResponse> getListShowTimeFilterCinema(FilterCinema filterCinema);
    List<CinemaShowTimeResponse> getListShowTimeFilterMovie(FilterMovie filterMovie);
    List<ShowTimeDto> getShowTimeDto(String movieName, String cinemaName, LocalDate date);
    void deleteShowTime(Long id);

}
