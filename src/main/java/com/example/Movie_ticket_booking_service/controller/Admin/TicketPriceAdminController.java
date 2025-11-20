package com.example.Movie_ticket_booking_service.controller.Admin;

import com.example.Movie_ticket_booking_service.dto.request.TicketPriceRequest;
import com.example.Movie_ticket_booking_service.dto.response.ApiResponse;
import com.example.Movie_ticket_booking_service.dto.response.TicketPriceResponse;
import com.example.Movie_ticket_booking_service.entity.TicketPriceEntity;
import com.example.Movie_ticket_booking_service.service.TicketPriceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/price")
@RequiredArgsConstructor
public class TicketPriceAdminController {
    private final TicketPriceService ticketPriceService;

    @PostMapping
    ApiResponse<TicketPriceResponse> createTicketPrice(@Valid @RequestBody TicketPriceRequest ticketPriceRequest) {
        return ApiResponse.<TicketPriceResponse>builder()
                .result(ticketPriceService.createTicketPrice(ticketPriceRequest))
                .build();
    }

    @PutMapping("/{id}")
    ApiResponse<TicketPriceResponse> updateTicketPrice(@PathVariable Long id,
                                                       @Valid @RequestBody TicketPriceRequest ticketPriceRequest) {
        return ApiResponse.<TicketPriceResponse>builder()
                .result(ticketPriceService.updateTicketPrice(id ,ticketPriceRequest))
                .build();
    }

    @GetMapping
    ApiResponse<List<TicketPriceResponse>> getAllTicketPrice() {
        return ApiResponse.<List<TicketPriceResponse>>builder()
                .result(ticketPriceService.getAllTicketPrice())
                .build();
    }
}
