package com.example.Movie_ticket_booking_service.controller;

import com.example.Movie_ticket_booking_service.dto.request.InvoiceRequest;
import com.example.Movie_ticket_booking_service.dto.response.ApiResponse;
import com.example.Movie_ticket_booking_service.dto.response.InvoiceDetailResponse;
import com.example.Movie_ticket_booking_service.dto.response.InvoiceResponse;
import com.example.Movie_ticket_booking_service.service.InvoiceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/invoice")
@RequiredArgsConstructor
public class InvoiceController {
    private final InvoiceService invoiceService;

    @PostMapping()
    public ApiResponse<Void> createInvoice(@Valid @RequestBody InvoiceRequest invoiceRequest) {
        invoiceService.createInvoice(invoiceRequest);
        return ApiResponse.<Void>builder()
                .message("Invoice Created")
                .build();
    }

    @GetMapping("/list")
    public ApiResponse<List<InvoiceResponse>> getInvoiceList() {
        return ApiResponse.<List<InvoiceResponse>>builder()
                .result(invoiceService.getInvoiceList())
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<InvoiceDetailResponse> getInvoice(@PathVariable("id") Long id) {
        return ApiResponse.<InvoiceDetailResponse>builder()
                .result(invoiceService.getInvoice(id))
                .build();
    }
}
