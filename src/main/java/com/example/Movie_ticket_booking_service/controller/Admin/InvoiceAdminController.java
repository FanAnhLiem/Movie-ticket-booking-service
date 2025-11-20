package com.example.Movie_ticket_booking_service.controller.Admin;

import com.example.Movie_ticket_booking_service.dto.response.ApiResponse;
import com.example.Movie_ticket_booking_service.dto.response.InvoiceDetailAD;
import com.example.Movie_ticket_booking_service.dto.response.InvoiceDetailResponse;
import com.example.Movie_ticket_booking_service.dto.response.InvoiceSummary;
import com.example.Movie_ticket_booking_service.service.InvoiceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin/invoice")
@RequiredArgsConstructor
public class InvoiceAdminController {

    private final InvoiceService invoiceService;

    @GetMapping
    ApiResponse<List<InvoiceSummary>> getInvoices() {
        return ApiResponse.<List<InvoiceSummary>>builder()
                .result(invoiceService.getInvoiceSummaryList())
                .build();
    }

    @GetMapping("/{id}")
    ApiResponse<InvoiceDetailAD> getInvoiceDetail(@Valid @PathVariable Long id) {
        return ApiResponse.<InvoiceDetailAD>builder()
                .result(invoiceService.getInvoiceDetail(id))
                .build();
    }



}
