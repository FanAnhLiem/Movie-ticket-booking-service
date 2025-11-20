package com.example.Movie_ticket_booking_service.repository;

import com.example.Movie_ticket_booking_service.entity.ScreenRoomTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScreenRoomTypeRepository extends JpaRepository<ScreenRoomTypeEntity, Long> {
    @Query("SELECT s.id FROM MovieEntity m JOIN m.showTimes s WHERE m.id = :movieId")
    List<Long> findShowTimesIdsByMovieId(@Param("movieId") Long movieId);

    boolean existsByName(String name);
}
