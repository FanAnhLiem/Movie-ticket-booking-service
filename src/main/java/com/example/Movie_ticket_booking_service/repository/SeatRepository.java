package com.example.Movie_ticket_booking_service.repository;

import com.example.Movie_ticket_booking_service.entity.SeatEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SeatRepository extends JpaRepository<SeatEntity, Long> {

    List<SeatEntity> findByScreenRoom_Id(Long screenRoomId);

    int countByScreenRoom_Id(Long id);
}
