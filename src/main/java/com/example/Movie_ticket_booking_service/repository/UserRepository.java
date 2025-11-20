package com.example.Movie_ticket_booking_service.repository;

import com.example.Movie_ticket_booking_service.entity.RoleEntity;
import com.example.Movie_ticket_booking_service.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    boolean existsByEmail(String email);
    Optional<UserEntity> findByEmail(String email);
    List<UserEntity> findByRole_Name(String roleName);

}
