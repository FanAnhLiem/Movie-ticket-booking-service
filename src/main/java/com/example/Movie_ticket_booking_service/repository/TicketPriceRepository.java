package com.example.Movie_ticket_booking_service.repository;

import com.example.Movie_ticket_booking_service.entity.TicketPriceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TicketPriceRepository extends JpaRepository<TicketPriceEntity,Long> {

    Boolean existsBySpecialDayAndTimeFrameAndCinemaType_IdAndScreenRoomType_IdAndSeatType_Id(
            Boolean specialDay,
            String timeFrame,
            Long cinemaTypeId,
            Long screenRoomTypeId,
            Long seatTypeId);

    Optional<TicketPriceEntity> findBySpecialDayAndTimeFrameAndCinemaType_IdAndScreenRoomType_IdAndSeatType_Id(
            Boolean specialDay,
            String timeFrame,
            Long cinemaTypeId,
            Long screenRoomTypeId,
            Long seatTypeId
    );
}
