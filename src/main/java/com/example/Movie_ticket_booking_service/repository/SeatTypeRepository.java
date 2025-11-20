package com.example.Movie_ticket_booking_service.repository;

import com.example.Movie_ticket_booking_service.entity.SeatTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SeatTypeRepository extends JpaRepository<SeatTypeEntity, Long> {

    boolean existsByName(String name);
    Optional<SeatTypeEntity> findByName(String name);
}
