package com.example.Movie_ticket_booking_service.controller;

import com.example.Movie_ticket_booking_service.dto.request.TokenRequest;
import com.example.Movie_ticket_booking_service.dto.request.UserLoginDTO;
import com.example.Movie_ticket_booking_service.dto.request.UserRequest;
import com.example.Movie_ticket_booking_service.dto.response.ApiResponse;
import com.example.Movie_ticket_booking_service.dto.response.TokenResponse;
import com.example.Movie_ticket_booking_service.service.AuthenticationService;
import com.example.Movie_ticket_booking_service.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationController {
    AuthenticationService authenticationService;
    AuthenticationService.TwoFactorService twoFactorService;
    UserService userService;

    @PostMapping("/register")
    public ApiResponse<TokenResponse> CreateUser(@Valid @RequestBody UserRequest userRequest) {
        return ApiResponse.<TokenResponse>builder()
                .result(userService.createUser(userRequest))
                .message("Đăng ký thành công")
                .build();
    }

    @PostMapping("/logout")
    public ApiResponse<String> logoutToken(){
        return ApiResponse.<String>builder()
                .result(authenticationService.logout())
                .build();
    }

    @PostMapping("/login")
    public ApiResponse<TokenResponse> login(@Valid @RequestBody UserLoginDTO userLoginDTO) {
        TokenResponse tokenResponse = authenticationService.login(userLoginDTO);
        return ApiResponse.<TokenResponse>builder()
                .result(tokenResponse)
                .build();
    }

}
