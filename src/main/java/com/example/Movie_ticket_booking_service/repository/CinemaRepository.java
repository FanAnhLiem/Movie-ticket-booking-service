package com.example.Movie_ticket_booking_service.repository;

import com.example.Movie_ticket_booking_service.entity.CinemaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CinemaRepository extends JpaRepository<CinemaEntity, Long> {
    Optional<CinemaEntity> findByName(String name);

    @Query("SELECT m FROM CinemaEntity m JOIN FETCH m.screenRooms WHERE m.id = :id")
    Optional<CinemaEntity> findCinemaWithScreenRooms(@Param("id") Long id);

    List<CinemaEntity> findByAddressAndStatusTrue(String address);

    Boolean existsByName(String name);
}
