package com.example.Movie_ticket_booking_service.service.Impl;

import com.example.Movie_ticket_booking_service.security.CustomUserDetailsService;
import com.example.Movie_ticket_booking_service.security.JwtTokenUtil;
import com.example.Movie_ticket_booking_service.entity.UserEntity;
import com.example.Movie_ticket_booking_service.exception.AppException;
import com.example.Movie_ticket_booking_service.exception.ErrorCode;
import com.example.Movie_ticket_booking_service.dto.request.UserLoginDTO;
import com.example.Movie_ticket_booking_service.dto.request.TokenRequest;
import com.example.Movie_ticket_booking_service.dto.response.TokenResponse;
import com.example.Movie_ticket_booking_service.repository.UserRepository;
import com.example.Movie_ticket_booking_service.service.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationServiceImpl implements AuthenticationService {
    UserRepository userRepository;
    PasswordEncoder passwordEncoder;
    JwtTokenUtil jwtTokenUtil;
    CustomUserDetailsService customUserDetailsService;
    RedisService redisService;
    HttpServletRequest request;

    @Override
    public TokenResponse login(UserLoginDTO userLoginDTO) {
        UserEntity userEntity = userRepository.findByEmail(userLoginDTO.getEmail()).get();
        boolean authenticated = passwordEncoder.matches(userLoginDTO.getPassword(), userEntity.getPassword());
        if(!authenticated) {throw new AppException(ErrorCode.WRONG_PASSWORD_OR_EMAIL);}
        return TokenResponse.builder()
                .token(jwtTokenUtil.generateAccessToken(userEntity.getEmail(), userEntity.getRole().getName()))
                .authenticated(authenticated)
                .build();

    }

    @Override
    public String logout() {
        String token = extractToken(request);
        if (token == null) return "logout failed";
        String jwi = jwtTokenUtil.extractJwtId(token);
        Instant exp = jwtTokenUtil.extractExpirationTime(token);
        if(jwi == null || exp == null) {
            return "logout failed";
        }
        redisService.blackList(jwi, exp);
        return "logout successful";
    }

    private String extractToken(HttpServletRequest req) {
        String h = req.getHeader("Authorization");
        return (h != null && h.startsWith("Bearer ")) ? h.substring(7) : null;
    }



}
