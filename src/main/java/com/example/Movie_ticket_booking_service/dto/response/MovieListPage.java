package com.example.Movie_ticket_booking_service.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;
@Data
@Builder
public class MovieListPage {
    private List<MoviePage> movieList;
    private int totalPages;
}
