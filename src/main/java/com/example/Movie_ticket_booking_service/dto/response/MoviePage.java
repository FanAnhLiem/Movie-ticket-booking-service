package com.example.Movie_ticket_booking_service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class MoviePage {
    private Long id;
    private String name;
    private String posterUrl;
    private int yearRelease;
    private String category;
    private String showSchedule;
    private String status;
    private String creatAt;
}
