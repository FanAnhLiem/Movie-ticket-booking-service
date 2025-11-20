package com.example.Movie_ticket_booking_service.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AssignRoleRequest {
    @JsonProperty("user_id")
    private Long userId;

    @JsonProperty("role_id")
    private Long roleId;
}
