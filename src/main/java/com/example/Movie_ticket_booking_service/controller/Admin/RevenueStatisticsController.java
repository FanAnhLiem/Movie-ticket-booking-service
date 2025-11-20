package com.example.Movie_ticket_booking_service.controller.Admin;

import com.example.Movie_ticket_booking_service.dto.response.*;
import com.example.Movie_ticket_booking_service.service.Impl.PriceService;
import com.example.Movie_ticket_booking_service.service.RevenueStatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/revenue")
@RequiredArgsConstructor
public class RevenueStatisticsController {
    private final RevenueStatisticsService revenueStatisticsService;

    @GetMapping("/dashbroad")
    ApiResponse<RevenueDashboardResponse> getDashboardRevenue(){
        return ApiResponse.<RevenueDashboardResponse>builder()
                .result(revenueStatisticsService.getDashboardRevenue())
                .build();
    }

    @GetMapping("/statistics/list")
    ApiResponse<List<StatisticSummary>> getRevenueList(){
        return ApiResponse.<List<StatisticSummary>>builder()
                .result(revenueStatisticsService.getRevenueList())
                .build();
    }

    @GetMapping("/movie")
    ApiResponse<List<MovieRevenue>> getMovieRevenue(@RequestParam("startDay")
                                                    @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate startDay,
                                                    @RequestParam("endDay")
                                                    @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate endDay){
        return ApiResponse.<List<MovieRevenue>>builder()
                .result(revenueStatisticsService.getMovieRevenue(startDay, endDay))
                .build();
    }

    @GetMapping("/cinema")
    ApiResponse<List<CinemaRevenue>> getCinemaRevenue(@RequestParam("startDay")
                                                      @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate startDay,
                                                      @RequestParam("endDay")
                                                      @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate endDay){
        return ApiResponse.<List<CinemaRevenue>>builder()
                .result(revenueStatisticsService.getCinemaRevenue(startDay, endDay))
                .build();
    }

}
