package com.example.Movie_ticket_booking_service.controller.Staff;

import com.example.Movie_ticket_booking_service.dto.response.ApiResponse;
import com.example.Movie_ticket_booking_service.dto.response.InvoiceDetailResponse;
import com.example.Movie_ticket_booking_service.dto.response.InvoiceResponse;
import com.example.Movie_ticket_booking_service.service.InvoiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/staff")
@RequiredArgsConstructor
public class StaffController {
    private final InvoiceService invoiceService;

    @GetMapping("/invoice")
    public ApiResponse<InvoiceDetailResponse> getInvoice(@RequestParam("booking-code") String bookingCode) {
        return ApiResponse.<InvoiceDetailResponse>builder()
                .result(invoiceService.getInvoiceByBookingCode(bookingCode))
                .build();
    }

    @PostMapping("/print/{invoiceId}")
    public ApiResponse<Void> checkIn(@PathVariable Long invoiceId) {
        invoiceService.checkInInvoice(invoiceId);
        return ApiResponse.<Void>builder()
                .message("In thanh công")
                .build();
    }
}
