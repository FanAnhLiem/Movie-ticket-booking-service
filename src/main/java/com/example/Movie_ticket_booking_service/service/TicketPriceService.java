package com.example.Movie_ticket_booking_service.service;

import com.example.Movie_ticket_booking_service.dto.request.TicketPriceRequest;
import com.example.Movie_ticket_booking_service.dto.response.TicketPriceResponse;
import com.example.Movie_ticket_booking_service.entity.TicketPriceEntity;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface TicketPriceService {
    TicketPriceResponse createTicketPrice(TicketPriceRequest ticketPriceRequest);

    TicketPriceEntity getSeatTicketPrice(Long cinemaTypeId,
                                         Long screenRoomTypeId,
                                         Long seatTypeId,
                                         LocalDate date,
                                         LocalTime time);

    TicketPriceResponse updateTicketPrice(Long id, TicketPriceRequest ticketPriceRequest);

    List<TicketPriceResponse> getAllTicketPrice();
}
