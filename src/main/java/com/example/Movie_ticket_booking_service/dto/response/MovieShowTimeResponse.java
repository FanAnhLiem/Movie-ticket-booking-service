package com.example.Movie_ticket_booking_service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MovieShowTimeResponse {
    private Long movieId;
    private String movieName;
    private String posterUrl;
    private List<ShowTimeSummaryResponse> showTimeSummaryResponseList;

    @Data
    public static class ShowTimeSummaryResponse{
        private Long showTimeId;
        private LocalTime startTime;
        private LocalTime endTime;
    }
}
