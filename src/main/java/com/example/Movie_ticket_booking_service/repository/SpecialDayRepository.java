package com.example.Movie_ticket_booking_service.repository;

import com.example.Movie_ticket_booking_service.dto.request.SpecialDayRequest;
import com.example.Movie_ticket_booking_service.entity.SpecialDayEntity;
import jakarta.validation.Valid;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestBody;

@Repository
public interface SpecialDayRepository extends JpaRepository<SpecialDayEntity, Long> {
    Boolean existsByMonthAndDay(int month, int day);
}
