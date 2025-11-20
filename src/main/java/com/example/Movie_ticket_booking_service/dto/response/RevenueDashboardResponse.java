package com.example.Movie_ticket_booking_service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.List;
@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class RevenueDashboardResponse {
    private int todayTicket;
    private BigDecimal todayRevenue;
    private BigDecimal monthRevenue;
    private int monthTicket;

}

