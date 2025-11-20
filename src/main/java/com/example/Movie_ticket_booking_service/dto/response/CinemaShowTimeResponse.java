package com.example.Movie_ticket_booking_service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CinemaShowTimeResponse {
    private Long cinemaId;
    private String cinemaName;
    private List<ShowTimeSummaryResponse> showTimeSummaryResponseList;

    @Data
    public static class ShowTimeSummaryResponse{
        private Long showTimeId;
        private LocalTime startTime;
        private LocalTime endTime;
    }
}
