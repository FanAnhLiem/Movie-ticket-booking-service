package com.example.Movie_ticket_booking_service.exception;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter

public class AppException extends RuntimeException {
    public AppException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    private final ErrorCode errorCode;
}
