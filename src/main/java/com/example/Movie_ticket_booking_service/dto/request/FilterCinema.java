package com.example.Movie_ticket_booking_service.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDate;
@Data
public class FilterCinema {

    @JsonProperty("cinema_id")
    private Long id;

    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate date;
}
