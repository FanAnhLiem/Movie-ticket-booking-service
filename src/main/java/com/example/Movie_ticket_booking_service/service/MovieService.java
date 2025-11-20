package com.example.Movie_ticket_booking_service.service;

import com.example.Movie_ticket_booking_service.dto.request.FilterMovie;
import com.example.Movie_ticket_booking_service.dto.request.MovieRequest;
import com.example.Movie_ticket_booking_service.dto.response.MovieDetailResponse;
import com.example.Movie_ticket_booking_service.dto.response.MoviePage;
import com.example.Movie_ticket_booking_service.dto.response.MovieResponse;
import com.example.Movie_ticket_booking_service.dto.response.MovieSummaryResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

public interface MovieService {
    MovieDetailResponse createMovie(MovieRequest movieRequest, MultipartFile thumbnailFile);
    MovieDetailResponse updateMovie(Long id , MovieRequest movieRequest, MultipartFile posterFile);
    void updateIsActiveMovie(Long id);
    void deleteMovie(Long id);
    MovieDetailResponse getMovie(Long id);
    List<MovieSummaryResponse> getAllMovies();
    List<MovieSummaryResponse> getNowPlayingMovies();
    List<MovieSummaryResponse> getUpcomingMovies();
    List<String> getListCinemaAddress(FilterMovie filterMovie);
    Page<MoviePage> getMoviePage(PageRequest pageRequest);
    List<MovieResponse> getMovieShowDay(LocalDate date);
}
