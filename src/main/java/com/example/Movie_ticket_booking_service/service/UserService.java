package com.example.Movie_ticket_booking_service.service;

import com.example.Movie_ticket_booking_service.dto.request.AssignRoleRequest;
import com.example.Movie_ticket_booking_service.dto.request.UpdateUserRequest;
import com.example.Movie_ticket_booking_service.dto.request.UserRequest;
import com.example.Movie_ticket_booking_service.dto.request.VerifyCodeRequest;
import com.example.Movie_ticket_booking_service.dto.response.TokenResponse;
import com.example.Movie_ticket_booking_service.dto.response.TwoFactorResponse;
import com.example.Movie_ticket_booking_service.dto.response.UserResponse;
import com.example.Movie_ticket_booking_service.dto.response.UserSummaryResponse;

import java.util.List;

public interface UserService {
    TokenResponse createUser(UserRequest user);
    TwoFactorResponse enableTwoFactor();
    TokenResponse verifyCode(VerifyCodeRequest verifyCodeRequest);
    UserResponse updateUser(UpdateUserRequest user);
    UserResponse getInfo();
    UserResponse assignRoleUser(Long userId, Long roleId);
    List<UserSummaryResponse> getUsers();

}
