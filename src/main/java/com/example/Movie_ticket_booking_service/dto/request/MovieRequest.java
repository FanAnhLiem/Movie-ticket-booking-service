package com.example.Movie_ticket_booking_service.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
@Data
public class MovieRequest {
    private String name;
    private String description;
    private int duration;
    private String category;
    private String country;
    private String director;
    private String actors;
    private int ageLimit;
    private String trailer;

    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDate releaseDate;

    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDate endDate;
}
