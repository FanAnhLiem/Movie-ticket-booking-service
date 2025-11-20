package com.example.Movie_ticket_booking_service.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserSummaryResponse {
    private Long id;
    private String email;
    private String name;
    private String phone;
    private String birthday;
    private String status;
    private String roleName;
}
