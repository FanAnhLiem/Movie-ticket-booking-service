package com.example.Movie_ticket_booking_service.controller.Admin;

import com.example.Movie_ticket_booking_service.dto.request.AssignRoleRequest;
import com.example.Movie_ticket_booking_service.dto.request.UpdateUserRequest;
import com.example.Movie_ticket_booking_service.dto.request.UserRequest;
import com.example.Movie_ticket_booking_service.dto.response.ApiResponse;
import com.example.Movie_ticket_booking_service.dto.response.RoleResponse;
import com.example.Movie_ticket_booking_service.dto.response.UserResponse;
import com.example.Movie_ticket_booking_service.dto.response.UserSummaryResponse;
import com.example.Movie_ticket_booking_service.service.RoleService;
import com.example.Movie_ticket_booking_service.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/account")
@RequiredArgsConstructor
public class UserManagementController {
    private final UserService userService;
    private final RoleService roleService;

    @PutMapping("/user/{userId}/role/{roleId}")
    public ApiResponse<UserResponse> assignRoleUser(@PathVariable Long userId,
                                                    @PathVariable Long roleId) {
        return ApiResponse.<UserResponse>builder()
                .result(userService.assignRoleUser(userId, roleId))
                .build();
    }

    @GetMapping("")
    public ApiResponse<List<UserSummaryResponse>> getUsers() {
        return ApiResponse.<List<UserSummaryResponse>>builder()
                .result(userService.getUsers())
                .build();
    }

//    @GetMapping("/user/{id}")
//    public ApiResponse<UserRespons> getUsers() {
//        return ApiResponse.<List<UserSummaryResponse>>builder()
//                .result(userService.getUsers())
//                .build();
//    }


    @GetMapping("/role")
    ApiResponse<List<RoleResponse>> getAllRoles() {
        return ApiResponse.<List<RoleResponse>>builder()
                .result(roleService.getAllRoles())
                .build();
    }
}
