package com.example.Movie_ticket_booking_service.repository;

import com.example.Movie_ticket_booking_service.entity.CinemaTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CinemaTypeRepository extends JpaRepository<CinemaTypeEntity, Long> {
    Optional<CinemaTypeEntity> findByName(String name);
}
