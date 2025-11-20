package com.example.Movie_ticket_booking_service.controller.Admin;

import com.example.Movie_ticket_booking_service.dto.request.SpecialDayRequest;
import com.example.Movie_ticket_booking_service.dto.response.ApiResponse;
import com.example.Movie_ticket_booking_service.entity.SpecialDayEntity;
import com.example.Movie_ticket_booking_service.service.Impl.SpecialDayService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/specialDay")
@RequiredArgsConstructor
public class SpecialDayController {
    private final SpecialDayService specialDayService;

    @PostMapping()
    public ApiResponse<SpecialDayEntity> createSpecialDay(@Valid @RequestBody SpecialDayRequest specialDayRequest) {
        return ApiResponse.<SpecialDayEntity>builder()
                .result(specialDayService.createSpecialDay(specialDayRequest))
                .build();
    }

    @PutMapping("/{id}")
    public ApiResponse<SpecialDayEntity> updateSpecialDay(@PathVariable("id") Long id,
                                                          @Valid @RequestBody SpecialDayRequest specialDayRequest) {
        return ApiResponse.<SpecialDayEntity>builder()
                .result(specialDayService.updateSpecialDay(id, specialDayRequest))
                .build();
    }

    @GetMapping()
    public ApiResponse<List<SpecialDayEntity>> getAllSpecialDay() {
        return ApiResponse.<List<SpecialDayEntity>>builder()
                .result(specialDayService.getAllSpecialDay())
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<SpecialDayEntity> getSpecialDay(@PathVariable("id") Long id) {
        return ApiResponse.<SpecialDayEntity>builder()
                .result(specialDayService.getSpecialDay(id))
                .build();
    }

    @DeleteMapping("/{id}")
    ApiResponse<?> deleteSpecialDay(@PathVariable("id") Long id){
        specialDayService.deleteSpecialDay(id);
        return ApiResponse.<SpecialDayEntity>builder()
                .message("Deleted successfully")
                .build();
    }
}
