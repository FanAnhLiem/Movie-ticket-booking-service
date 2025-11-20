package com.example.Movie_ticket_booking_service.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
@Getter
public enum ErrorCode {
    // ===== Generic / System =====
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),

    // ===== Auth / User: 1000–1099 =====
    UNAUTHENTICATED(1000, "Unauthenticated", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(1001, "You do not have permission", HttpStatus.FORBIDDEN),
    WRONG_PASSWORD_OR_EMAIL(1002, "Wrong email or password", HttpStatus.BAD_REQUEST),
    INVALID_JWT_TOKEN(1003, "Invalid JWT token", HttpStatus.BAD_REQUEST),
    TOKEN_EXPIRED(1004, "JWT token expired", HttpStatus.UNAUTHORIZED),
    USER_EXISTED(1010, "User existed", HttpStatus.BAD_REQUEST),
    USER_NOT_EXISTED(1011, "User not existed", HttpStatus.NOT_FOUND),

    // ===== Role / Authorization: 1100–1199 =====
    ROLE_NOT_EXISTED(1100, "Role not existed", HttpStatus.NOT_FOUND),
    ROLE_EMPTY(1101, "Role empty", HttpStatus.BAD_REQUEST),
    CANT_ASSIGN_ROLE(1102, "Can't assign role with administrator status", HttpStatus.BAD_REQUEST),

    // ===== Movie & Category & Special Day: 1200–1299 =====
    MOVIE_EXISTED(1200, "Movie existed", HttpStatus.BAD_REQUEST),
    MOVIE_NOT_EXISTED(1201, "Movie not existed", HttpStatus.NOT_FOUND),
    MOVIE_CANT_DELETE(1202, "Movie can't be deleted", HttpStatus.BAD_REQUEST),
    INVALID_DATE_RANGE(1202, "The release_date cannot be later than the end_date", HttpStatus.BAD_REQUEST),

    MOVIE_CATEGORY_EXISTED(1210, "Movie category existed", HttpStatus.BAD_REQUEST),
    MOVIE_CATEGORY_NOT_EXISTED(1211, "Movie category not existed", HttpStatus.NOT_FOUND),
    CREATE_MOVIE_CATEGORY_FAIL(1212, "Create movie category failed", HttpStatus.BAD_REQUEST),

    SPECIAL_DAY_EXISTED(1220, "Special day existed", HttpStatus.BAD_REQUEST),
    SPECIAL_DAY_NOT_EXISTED(1221, "Special day not existed", HttpStatus.NOT_FOUND),

    DATA_VIOLATION(1290, "Data violation", HttpStatus.BAD_REQUEST),

    // ===== Cinema / Screen Room / Seat / ShowTime: 1300–1399 =====
    CINEMA_EXISTED(1300, "Cinema existed", HttpStatus.BAD_REQUEST),
    CINEMA_NOT_EXISTED(1301, "Cinema not existed", HttpStatus.NOT_FOUND),
    CINEMA_TYPE_NOT_EXISTED(1302, "Cinema type not existed", HttpStatus.NOT_FOUND),

    SCREEN_ROOM_EXISTED(1310, "Screen room existed", HttpStatus.BAD_REQUEST),
    SCREEN_ROOM_NOT_EXISTED(1311, "Screen room not existed", HttpStatus.NOT_FOUND),

    SCREEN_ROOM_TYPE_EXISTED(1320, "Screen room type existed", HttpStatus.BAD_REQUEST),
    SCREEN_ROOM_TYPE_NOT_EXISTED(1321, "Screen room type not existed", HttpStatus.NOT_FOUND),

    SEAT_TYPE_EXISTED(1330, "Seat type existed", HttpStatus.BAD_REQUEST),
    SEAT_TYPE_NOT_EXISTED(1331, "Seat type not existed", HttpStatus.NOT_FOUND),
    SEAT_TYPE_EMPTY(1332, "Seat type empty", HttpStatus.BAD_REQUEST),
    SEAT_NOT_EXISTED(1333, "Seat not existed", HttpStatus.NOT_FOUND),

    SHOW_TIME_OVERLAP(1340, "Show time overlap", HttpStatus.BAD_REQUEST),
    SHOW_TIME_NOT_EXISTED(1341, "Show time not existed", HttpStatus.NOT_FOUND),
    INVALID_TIME_RANGE(1340, "Invalid time range", HttpStatus.BAD_REQUEST),

    // ===== Invoice / Payment: 1400–1499 =====
    INVOICE_NOT_EXISTED(1410, "Invoice not existed", HttpStatus.NOT_FOUND),
    ORDER_CREATE_FAILED(1420, "Create order failed", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_PRICE(1422, "Invalid price", HttpStatus.BAD_REQUEST),
    TICKET_PRICE_EXISTED(1430, "Ticket price existed", HttpStatus.BAD_REQUEST),
    TICKET_PRICE_NOT_EXISTED(1431, "Ticket price not existed", HttpStatus.NOT_FOUND),

    // ===== File / Upload: 1500–1599 =====
    UPLOAD_FAILED(1500, "Upload file failed", HttpStatus.INTERNAL_SERVER_ERROR),
    FILE_REQUIRED(1501, "Poster file is required", HttpStatus.BAD_REQUEST),
    FILE_TOO_LARGE(1502, "File is too large! Maximum size is 10MB", HttpStatus.PAYLOAD_TOO_LARGE),
    UNSUPPORTED_FILE_TYPE(1503, "File must be an image", HttpStatus.UNSUPPORTED_MEDIA_TYPE),

    ;

    ErrorCode(int code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }

    private final int code;
    private final String message;
    private final HttpStatusCode statusCode;
}
