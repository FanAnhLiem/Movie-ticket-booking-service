package com.example.Movie_ticket_booking_service.enums;

import lombok.Getter;

@Getter
public enum CinemaType {
    NORMAL("NORMAL", 1.0),
    PREMIUM("PREMIUM", 1.1),
    LUXURY("LUXURY", 1.2);

    private final String name;
    private final Double priceFactor;

    CinemaType(String name, Double priceFactor) {
        this.name = name;
        this.priceFactor = priceFactor;
    }
}
