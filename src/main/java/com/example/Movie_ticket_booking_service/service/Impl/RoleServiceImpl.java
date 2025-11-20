package com.example.Movie_ticket_booking_service.service.Impl;

import com.example.Movie_ticket_booking_service.dto.response.RoleResponse;
import com.example.Movie_ticket_booking_service.entity.RoleEntity;
import com.example.Movie_ticket_booking_service.exception.AppException;
import com.example.Movie_ticket_booking_service.exception.ErrorCode;
import com.example.Movie_ticket_booking_service.repository.RoleRepository;
import com.example.Movie_ticket_booking_service.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor

public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;
    private final ModelMapper modelMapper;


    @Override
    public List<RoleResponse> getAllRoles() {
        List<RoleEntity> roles = roleRepository.findAll();
        if(roles.isEmpty()){
            throw new AppException(ErrorCode.ROLE_NOT_EXISTED);
        }
        List<RoleResponse> roleResponseList = roles.stream()
                .map(r -> modelMapper.map(r, RoleResponse.class))
                .toList();
        return roleResponseList;
    }
}
