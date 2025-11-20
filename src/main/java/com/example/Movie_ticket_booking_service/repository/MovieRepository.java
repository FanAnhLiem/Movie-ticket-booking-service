package com.example.Movie_ticket_booking_service.repository;

import com.example.Movie_ticket_booking_service.entity.MovieEntity;
import com.example.Movie_ticket_booking_service.entity.ShowTimeEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
@Repository
public interface MovieRepository extends JpaRepository<MovieEntity, Long> {
    Optional<MovieEntity> findByName(String name);

    @Query("SELECT m FROM MovieEntity m WHERE m.releaseDate <= :today AND m.endDate >= :today AND m.status IS TRUE ")
    List<MovieEntity> findNowPlayingMovies(@Param("today") LocalDate today);

    @Query("SELECT m FROM MovieEntity m WHERE m.releaseDate > :today AND m.status IS TRUE ")
    List<MovieEntity> findUpcomingMovies(@Param("today") LocalDate today);

    @Query("SELECT s FROM MovieEntity m JOIN m.showTimes s WHERE m.id = :movieId AND s.startTime >= :date ")
    List<ShowTimeEntity> findShowTimesByMovieId(@Param("movieId") Long movieId, @Param("date") LocalDateTime date);

    List<MovieEntity> findTop100ByOrderByEndDateDesc();

    Page<MovieEntity> findAll(Pageable pageable);





}
