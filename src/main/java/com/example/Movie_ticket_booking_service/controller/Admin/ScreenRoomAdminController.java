package com.example.Movie_ticket_booking_service.controller.Admin;

import com.example.Movie_ticket_booking_service.dto.request.ScreenRoomRequest;
import com.example.Movie_ticket_booking_service.dto.request.ScreenRoomTypeRequest;
import com.example.Movie_ticket_booking_service.dto.response.ApiResponse;
import com.example.Movie_ticket_booking_service.dto.response.ScreenRoomDetailResponse;
import com.example.Movie_ticket_booking_service.dto.response.ScreenRoomResponse;
import com.example.Movie_ticket_booking_service.dto.response.ScreenRoomTypeResponse;
import com.example.Movie_ticket_booking_service.entity.ScreenRoomTypeEntity;
import com.example.Movie_ticket_booking_service.service.Impl.ScreenRoomServiceImpl;
import com.example.Movie_ticket_booking_service.service.Impl.ScreenRoomTypeServiceImpl;
import com.example.Movie_ticket_booking_service.service.ScreenRoomService;
import com.example.Movie_ticket_booking_service.service.ScreenRoomTypeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/screenRoom")
@RequiredArgsConstructor
public class ScreenRoomAdminController {
    private final ScreenRoomService screenRoomService;

    @PostMapping
    public ApiResponse<ScreenRoomResponse> createScreenRoom(@Valid @RequestBody ScreenRoomRequest screenRoomRequest) {
        return ApiResponse.<ScreenRoomResponse>builder()
                .result(screenRoomService.createScreenRoom(screenRoomRequest))
                .build();
    }

    @PutMapping("/{id}")
    public ApiResponse<ScreenRoomResponse> updateScreenRoom(@Valid @PathVariable Long id,
                                                                    @RequestBody ScreenRoomRequest screenRoomRequest) {
        return ApiResponse.<ScreenRoomResponse>builder()
                .result(screenRoomService.updateScreenRoom(id, screenRoomRequest))
                .build();
    }

    @GetMapping("/cinema/{cinemaId}")
    public ApiResponse<List<ScreenRoomDetailResponse>> getScreenRoomList(@Valid @PathVariable("cinemaId") Long id) {
        return ApiResponse.<List<ScreenRoomDetailResponse>>builder()
                .result(screenRoomService.getScreenRoomList(id))
                .build();
    }

    @GetMapping("/active/cinema/{cinemaId}")
    public ApiResponse<List<ScreenRoomDetailResponse>> getScreenRoomActiveList(@Valid @PathVariable("cinemaId") Long id) {
        return ApiResponse.<List<ScreenRoomDetailResponse>>builder()
                .result(screenRoomService.getScreenRoomActiveList(id))
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<ScreenRoomResponse> getScreenRoom(@Valid @PathVariable Long id) {
        return ApiResponse.<ScreenRoomResponse>builder()
                .result(screenRoomService.getScreenRoomDetail(id))
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Boolean> deleteScreenRoom(@Valid @PathVariable Long id) {
        boolean deleted = screenRoomService.deleteScreenRoom(id);
        return ApiResponse.<Boolean>builder()
                .result(deleted)
                .message(deleted ? "Screen Room delete successfully"
                                 : "Screen Room delete failed")
                .build();
    }
}
