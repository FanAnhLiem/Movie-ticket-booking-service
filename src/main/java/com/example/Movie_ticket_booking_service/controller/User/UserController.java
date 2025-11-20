package com.example.Movie_ticket_booking_service.controller.User;

import com.example.Movie_ticket_booking_service.dto.request.TokenRequest;
import com.example.Movie_ticket_booking_service.dto.request.UpdateUserRequest;
import com.example.Movie_ticket_booking_service.dto.request.VerifyCodeRequest;
import com.example.Movie_ticket_booking_service.dto.response.ApiResponse;
import com.example.Movie_ticket_booking_service.dto.response.TokenResponse;
import com.example.Movie_ticket_booking_service.dto.response.TwoFactorResponse;
import com.example.Movie_ticket_booking_service.dto.response.UserResponse;
import com.example.Movie_ticket_booking_service.service.AuthenticationService;
import com.example.Movie_ticket_booking_service.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {
    UserService userService;
    AuthenticationService authenticationService;


    @PostMapping("/enable-2fa")
    public ApiResponse<TwoFactorResponse> enable2fa() {
        TwoFactorResponse twoFactorResponse = userService.enableTwoFactor();
        if(!twoFactorResponse.isSuccess()) {
            return ApiResponse.<TwoFactorResponse>builder()
                    .result(twoFactorResponse)
                    .message("enable two-factor failed")
                    .build();
        }
        return ApiResponse.<TwoFactorResponse>builder()
                .result(twoFactorResponse)
                .message("enable two-factor successful")
                .build();
    }

    @PostMapping("/verify-code-2fa")
    ApiResponse<TokenResponse> verifyCode(@Valid @RequestBody VerifyCodeRequest verifyCodeRequest) {
        TokenResponse tokenResponse = userService.verifyCode(verifyCodeRequest);
        if(tokenResponse == null) {
            return ApiResponse.<TokenResponse>builder()
                    .message("verify code failed")
                    .build();
        }
        return ApiResponse.<TokenResponse>builder()
                .result(tokenResponse)
                .message("verify code successful")
                .build();
    }

    @GetMapping("/info")
    public ApiResponse<UserResponse> getInfo() {
        return ApiResponse.<UserResponse>builder()
                .result(userService.getInfo())
                .build();
    }

    @PutMapping("/info")
    public ApiResponse<UserResponse> updateUser(@Valid @RequestBody UpdateUserRequest userRequest) {
        return ApiResponse.<UserResponse>builder()
                .result(userService.updateUser(userRequest))
                .build();
    }
}
