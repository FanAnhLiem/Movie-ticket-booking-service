package com.example.Movie_ticket_booking_service.controller;

import com.example.Movie_ticket_booking_service.dto.PaymentDto;
import com.example.Movie_ticket_booking_service.dto.response.ApiResponse;
import com.example.Movie_ticket_booking_service.dto.response.CheckOutResponse;
import com.example.Movie_ticket_booking_service.service.paymentServices.VNPayService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.hc.client5.http.impl.auth.AuthCacheKeeper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/vnpay")
@RequiredArgsConstructor
public class VnPayController {
    private final VNPayService vnpayService;

    @PostMapping("/payment")
    public ApiResponse<CheckOutResponse> createPayment(@Valid @RequestBody PaymentDto paymentDto) {
        String paymentUrl = vnpayService.createPayment(paymentDto);
        return ApiResponse.<CheckOutResponse>builder()
                .result(CheckOutResponse.builder()
                            .status("success")
                            .paymentUrl(paymentUrl)
                            .build())
                .build();
    }


    @GetMapping("/return")
    public ApiResponse<String> returnPayment(@RequestParam("vnp_ResponseCode") String responseCode,
                                                @RequestParam("vnp_TxnRef") String vnpTxnRef) {
        return ApiResponse.<String>builder()
                .result(vnpayService.handlePaymentReturn(responseCode, vnpTxnRef))
                .build();
    }
}
