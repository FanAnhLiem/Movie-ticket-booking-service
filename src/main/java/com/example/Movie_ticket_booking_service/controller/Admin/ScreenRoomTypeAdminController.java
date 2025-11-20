package com.example.Movie_ticket_booking_service.controller.Admin;

import com.example.Movie_ticket_booking_service.dto.request.ScreenRoomTypeRequest;
import com.example.Movie_ticket_booking_service.dto.response.*;
import com.example.Movie_ticket_booking_service.service.ScreenRoomTypeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/screenRoomType")
@RequiredArgsConstructor
public class ScreenRoomTypeAdminController {
    private final ScreenRoomTypeService screenRoomTypeService;

    @PostMapping
    public ApiResponse<ScreenRoomTypeResponse> createScreenRoomType(@Valid @RequestBody ScreenRoomTypeRequest screenRoomTypeRequest) {
        return ApiResponse.<ScreenRoomTypeResponse>builder()
                .result(screenRoomTypeService.createScreenRoomType(screenRoomTypeRequest))
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<ScreenRoomTypeResponse> getScreenRoomType(@PathVariable("id") Long id) {
        return ApiResponse.<ScreenRoomTypeResponse>builder()
                .result(screenRoomTypeService.getScreenRoomType(id))
                .build();
    }

    @GetMapping
    public ApiResponse<List<ScreenRoomTypeResponse>> getAllScreenRoomTypes() {
        return ApiResponse.<List<ScreenRoomTypeResponse>>builder()
                .result(screenRoomTypeService.getAllScreenRoomType())
                .build();
    }


    @PutMapping("/{id}")
    public ApiResponse<ScreenRoomTypeResponse> updateScreenRoomType(@Valid @PathVariable Long id,
                                                                   @RequestBody ScreenRoomTypeRequest screenRoomTypeRequest) {
        return ApiResponse.<ScreenRoomTypeResponse>builder()
                .result(screenRoomTypeService.updateScreenRoomType(id, screenRoomTypeRequest))
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Boolean> deleteScreenRoomType(@Valid @PathVariable Long id) {
        boolean deleted = screenRoomTypeService.deleteScreenRoomType(id);
        return ApiResponse.<Boolean>builder()
                .result(deleted)
                .message(deleted ? "Screen Room Type delete successfully"
                        : "Screen Room Type delete failed")
                .build();
    }
}
