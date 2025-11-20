package com.example.Movie_ticket_booking_service.repository;

import com.example.Movie_ticket_booking_service.entity.CinemaEntity;
import com.example.Movie_ticket_booking_service.entity.ScreenRoomEntity;
import com.example.Movie_ticket_booking_service.entity.ShowTimeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
@Repository
public interface ScreenRoomRepository extends JpaRepository<ScreenRoomEntity, Long> {
    @Query("SELECT st FROM ScreenRoomEntity sr JOIN sr.showTimes st JOIN FETCH st.movie m" +
            " WHERE sr.cinema.id = :cinemaId AND st.startTime >= :currenTime AND DATE(st.startTime) >= :date")
    List<ShowTimeEntity> findCinemaMovieShowTime(@Param("cinemaId") Long cinemaId,
                                                 @Param("currenTime") LocalDateTime currenTime,
                                                 @Param("date") LocalDate date);

    boolean existsByName(String name);

    List<ScreenRoomEntity> findByCinemaId(Long cinemaId);

    List<ScreenRoomEntity> findByCinemaIdAndStatusTrue(Long cinemaId);

    boolean existsByNameAndCinema_IdAndScreenRoomType_Id(String name, Long cinemaId, Long screenRoomTypeId);


}
