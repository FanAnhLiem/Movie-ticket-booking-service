package com.example.Movie_ticket_booking_service.service;

import com.example.Movie_ticket_booking_service.dto.response.RoleResponse;
import com.example.Movie_ticket_booking_service.entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoleService{
    List<RoleResponse> getAllRoles();
}
