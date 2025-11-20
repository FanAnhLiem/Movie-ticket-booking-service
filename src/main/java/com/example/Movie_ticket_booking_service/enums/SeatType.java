package com.example.Movie_ticket_booking_service.enums;

import lombok.Getter;

@Getter
public enum SeatType {
    NORMAL("STANDARD", 1.0),
    PREMIUM("VIP", 1.02),
    LUXURY("SWEETBOX", 1.05);

    private final String name;
    private final Double priceFactor;

    SeatType(String name, Double priceFactor) {
        this.name = name;
        this.priceFactor = priceFactor;
    }
}
