package com.example.Movie_ticket_booking_service.controller.Admin;

import com.example.Movie_ticket_booking_service.dto.request.ShowTimeRequest;
import com.example.Movie_ticket_booking_service.dto.response.ApiResponse;
import com.example.Movie_ticket_booking_service.dto.response.ShowTimeDTO;
import com.example.Movie_ticket_booking_service.dto.response.ShowTimeResponse;
import com.example.Movie_ticket_booking_service.service.ShowTimeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/admin/showtime")
@RequiredArgsConstructor
public class ShowTimeAdminController {
    private final ShowTimeService showTimeService;

    @PostMapping
    public ApiResponse<ShowTimeResponse> createShowTime(@Valid @RequestBody ShowTimeRequest showTimeRequest) {
        return ApiResponse.<ShowTimeResponse>builder()
                .result(showTimeService.createShowTime(showTimeRequest))
                .build();
    }

    @PutMapping("/{id}")
    public ApiResponse<ShowTimeResponse> updateShowTime(@PathVariable("id") Long id, @RequestBody ShowTimeRequest showTimeRequest) {
        return ApiResponse.<ShowTimeResponse>builder()
                .result(showTimeService.updateShowTime(showTimeRequest,id))
                .build();
    }


    @GetMapping("/screenroom/{id}")
    public ApiResponse<List<ShowTimeDTO>> getShowTimeList(@PathVariable Long id,
                                                          @RequestParam("date")
                                                          @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate date) {
        return ApiResponse.<List<ShowTimeDTO>>builder()
                .result(showTimeService.getShowTimeList(id, date))
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<ShowTimeResponse> getShowTime(@PathVariable Long id) {
        return ApiResponse.<ShowTimeResponse>builder()
                .result(showTimeService.getShowTime(id))
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteShowTime(@PathVariable Long id) {
        showTimeService.deleteShowTime(id);
        return ApiResponse.<Void>builder()
                .message("delete show time successfully")
                .build();
    }
}
