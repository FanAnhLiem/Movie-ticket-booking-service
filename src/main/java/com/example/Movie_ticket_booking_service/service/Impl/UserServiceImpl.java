package com.example.Movie_ticket_booking_service.service.Impl;

import com.example.Movie_ticket_booking_service.security.JwtTokenUtil;
import com.example.Movie_ticket_booking_service.dto.request.UpdateUserRequest;
import com.example.Movie_ticket_booking_service.dto.response.UserResponse;
import com.example.Movie_ticket_booking_service.dto.response.UserSummaryResponse;
import com.example.Movie_ticket_booking_service.entity.RoleEntity;
import com.example.Movie_ticket_booking_service.entity.UserEntity;
import com.example.Movie_ticket_booking_service.enums.PredefinedRoles;
import com.example.Movie_ticket_booking_service.exception.AppException;
import com.example.Movie_ticket_booking_service.exception.ErrorCode;
import com.example.Movie_ticket_booking_service.dto.request.UserRequest;
import com.example.Movie_ticket_booking_service.dto.request.VerifyCodeRequest;
import com.example.Movie_ticket_booking_service.dto.response.TokenResponse;
import com.example.Movie_ticket_booking_service.dto.response.TwoFactorResponse;
import com.example.Movie_ticket_booking_service.repository.RoleRepository;
import com.example.Movie_ticket_booking_service.repository.UserRepository;
import com.example.Movie_ticket_booking_service.service.AuthenticationService;
import com.example.Movie_ticket_booking_service.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationService.TwoFactorService twoFactorService;
    private final JwtTokenUtil jwtTokenUtil;
    @Override
    public TokenResponse createUser(UserRequest user) {
        if(userRepository.existsByEmail(user.getEmail())) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }
        UserEntity userEntity = modelMapper.map(user, UserEntity.class);
        userEntity.setPassword(passwordEncoder.encode(user.getPassword()));
        userEntity.setStatus(true);
        RoleEntity role = roleRepository.findByName(PredefinedRoles.USER).get();
        userEntity.setRole(role);
        try {
            return TokenResponse.builder()
                    .token(jwtTokenUtil.generateAccessToken(userRepository.save(userEntity).getEmail(), userEntity.getRole().getName()))
                    .authenticated(true)
                    .build();
        }catch (DataIntegrityViolationException e){
            throw new AppException(ErrorCode.DATA_VIOLATION);
        }
    }

    @Override
    public TwoFactorResponse enableTwoFactor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails user = (UserDetails) authentication.getPrincipal();
        String email = user.getUsername();
        UserEntity userEntity = userRepository.findByEmail(email)
                .orElseThrow(()-> new AppException(ErrorCode.USER_NOT_EXISTED));
        TwoFactorResponse twoFactorResponse = new TwoFactorResponse();
        if(userEntity.isTwoFactorEnabled()) {
            twoFactorResponse.setSuccess(false);
            return twoFactorResponse;
        }
        String secret =  twoFactorService.generateNewSecret();
        String qrCode = twoFactorService.generateQrCodeImage(secret, email);
        twoFactorResponse.setSuccess(true);
        twoFactorResponse.setQrCode(qrCode);
        twoFactorResponse.setToken(jwtTokenUtil.generateAccessToken(email, userEntity.getRole().getName()));
        userEntity.setTwoFactorSecret(secret);
        userRepository.save(userEntity);
        return twoFactorResponse;
    }

    @Override
    public TokenResponse verifyCode(VerifyCodeRequest verifyCodeRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails user = (UserDetails) authentication.getPrincipal();
        String email = user.getUsername();

        UserEntity userEntity = userRepository.findByEmail(email).get();
        if(twoFactorService.verifyCode(userEntity.getTwoFactorSecret(), verifyCodeRequest.getCode())) {
            userEntity.setTwoFactorEnabled(true);
            userRepository.save(userEntity);
            TokenResponse tokenResponse = new TokenResponse();
            tokenResponse.setToken(jwtTokenUtil.generateAccessToken(email, userEntity.getRole().getName()));
            tokenResponse.setAuthenticated(true);
            return tokenResponse;
        }
        return null;
    }

    @Override
    public UserResponse updateUser(UpdateUserRequest user) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserEntity userEntity = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        modelMapper.map(user,userEntity);
        UserResponse userResponse = modelMapper.map(userRepository.save(userEntity), UserResponse.class);
        userResponse.setRoleId(userEntity.getRole().getId());
        return  userResponse;
    }

    @Override
    public UserResponse getInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails user = (UserDetails) authentication.getPrincipal();
        String email = user.getUsername();

        UserEntity userEntity = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        UserResponse userResponse = modelMapper.map(userEntity, UserResponse.class);
        userResponse.setRoleId(userEntity.getRole().getId());
        return userResponse;
    }

    @Override
    public UserResponse assignRoleUser(Long userId, Long roleId) {
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        RoleEntity role = roleRepository.findById(roleId)
                .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_EXISTED));
        if(role.getName().equals(PredefinedRoles.ADMIN)) {
            throw new AppException(ErrorCode.CANT_ASSIGN_ROLE);
        }
        userEntity.setRole(role);
        try {
            userRepository.save(userEntity);
            UserResponse userResponse = modelMapper.map(userEntity, UserResponse.class);
            userResponse.setRoleId(role.getId());
            return userResponse;
        }catch (DataIntegrityViolationException e){
            throw new AppException(ErrorCode.DATA_VIOLATION);
        }
    }

    @Override
    public List<UserSummaryResponse> getUsers() {
        List<UserEntity> userEntityList = userRepository.findAll();
        try {
            List<UserSummaryResponse> userSummaryResponseList = userEntityList.stream()
                    .map(userEntity -> UserSummaryResponse.builder()
                            .id(userEntity.getId())
                            .email(userEntity.getEmail())
                            .name(userEntity.getName())
                            .roleName(userEntity.getRole().getName())
                            .birthday(userEntity.getBirthday())
                            .phone(userEntity.getPhone())
                            .status(userEntity.isStatus() ? "Đang hoạt động" : "Ngừng hoạt động")
                            .build())
                    .toList();
            return userSummaryResponseList;
        }catch (DataIntegrityViolationException e){
            throw new AppException(ErrorCode.DATA_VIOLATION);
        }
    }


}

