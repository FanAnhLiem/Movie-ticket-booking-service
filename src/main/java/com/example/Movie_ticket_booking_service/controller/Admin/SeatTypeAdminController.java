package com.example.Movie_ticket_booking_service.controller.Admin;

import com.example.Movie_ticket_booking_service.dto.SeatTypeResponse;
import com.example.Movie_ticket_booking_service.dto.request.SeatTypeRequest;
import com.example.Movie_ticket_booking_service.dto.response.ApiResponse;
import com.example.Movie_ticket_booking_service.service.SeatTypeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/seatType")
@RequiredArgsConstructor
public class SeatTypeAdminController {
    private final SeatTypeService seatTypeService;

    @PostMapping
    public ApiResponse<SeatTypeResponse> createSeatType(@RequestBody SeatTypeRequest seatTypeResponse) {
        return ApiResponse.<SeatTypeResponse>builder()
                .result(seatTypeService.createSeatType(seatTypeResponse))
                .build();
    }

    @PutMapping("/{id}")
    public ApiResponse<SeatTypeResponse> updateSeatType(@PathVariable("id") Long id, @RequestBody SeatTypeRequest seatTypeRequest) {
        return ApiResponse.<SeatTypeResponse>builder()
                .result(seatTypeService.updateSeatType(id, seatTypeRequest))
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<SeatTypeResponse> getSeatType(@PathVariable Long id) {
        return ApiResponse.<SeatTypeResponse>builder()
                .result(seatTypeService.getSeatType(id))
                .build();
    }

    @GetMapping
    public ApiResponse<List<SeatTypeResponse>> getAllSeatType() {
        return ApiResponse.<List<SeatTypeResponse>>builder()
                .result(seatTypeService.getAllSeatType())
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Boolean> deleteSeatType(@Valid @PathVariable Long id) {
        boolean deleted = seatTypeService.deleteSeatType(id);
        return ApiResponse.<Boolean>builder()
                .result(deleted)
                .message(deleted ? "Seat Type delete successfully"
                        : "Seat Type delete failed")
                .build();
    }
}
