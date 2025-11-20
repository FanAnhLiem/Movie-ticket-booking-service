package com.example.Movie_ticket_booking_service.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class UpdateUserRequest {
    private String name;
    private String phone;

    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate birthday;

}
