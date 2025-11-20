package com.example.Movie_ticket_booking_service.dto.response;

import com.example.Movie_ticket_booking_service.enums.SeatStatus;
import com.google.type.Decimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SeatShowTimeResponse {
    private Long id;
    private String seatCode;
    private BigDecimal price;
    private String status = SeatStatus.AVAILABLE;
    private String seatTypeName;
}
