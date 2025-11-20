package com.example.Movie_ticket_booking_service.dto.response;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TokenResponse {
    private String token;
    private boolean authenticated;
}
