package com.example.Movie_ticket_booking_service.service;

import com.example.Movie_ticket_booking_service.dto.response.CinemaRevenue;
import com.example.Movie_ticket_booking_service.dto.response.MovieRevenue;
import com.example.Movie_ticket_booking_service.dto.response.RevenueDashboardResponse;
import com.example.Movie_ticket_booking_service.dto.response.StatisticSummary;

import java.time.LocalDate;
import java.util.List;

public interface RevenueStatisticsService {
    RevenueDashboardResponse getDashboardRevenue();
    List<StatisticSummary> getRevenueList();
    List<MovieRevenue> getMovieRevenue(LocalDate startDate, LocalDate endDate);
    List<CinemaRevenue> getCinemaRevenue(LocalDate startDate, LocalDate endDate);
}
