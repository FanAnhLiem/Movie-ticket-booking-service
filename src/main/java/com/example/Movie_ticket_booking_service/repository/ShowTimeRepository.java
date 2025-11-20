package com.example.Movie_ticket_booking_service.repository;

import com.example.Movie_ticket_booking_service.entity.ScreenRoomEntity;
import com.example.Movie_ticket_booking_service.entity.ShowTimeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ShowTimeRepository extends JpaRepository<ShowTimeEntity, Long> {
    @Query("""
        select case when count(s) > 0 then true else false end
        from ShowTimeEntity s
        where s.screenRoom.id = :screenRoomId
          and s.showDate = :showDate
          and (:excludeId is null or s.id <> :excludeId)
          and s.startTime < :newEnd
          and s.endTime   > :newStart
    """)
    boolean existsOverlapping(
            @Param("screenRoomId") Long screenRoomId,
            @Param("showDate") LocalDate showDate,
            @Param("newStart") LocalTime newStart,
            @Param("newEnd") LocalTime newEnd,
            @Param("excludeId") Long excludeId //kiểm tra khi update (<> dấu phép so sánh khác nhau)
    ); //mệnh đề WHERE chỉ giữ lại những hàng có điều kiện đánh giá là TRUE.
    // Nếu bất kỳ một điều kiện trong chuỗi AND cho ra FALSE hoặc UNKNOWN (do NULL) thì cả hàng đó bị loại.

    List<ShowTimeEntity> findByScreenRoom_Cinema_IdAndShowDateAndStartTimeAfterAndStatusTrue(
            Long cinemaId,
            LocalDate date,
            LocalTime currentTime
    );

    List<ShowTimeEntity> findByMovie_IdAndShowDateAndStartTimeAfterAndStatusTrue(
            Long movieId,
            LocalDate date,
            LocalTime currentTime
    );

    List<ShowTimeEntity> findByScreenRoom_Cinema_AddressAndMovie_IdAndShowDateAndStartTimeAfterAndStatusTrue(
            String address,
            Long movieId,
            LocalDate date,
            LocalTime currentTime
    );

    List<ShowTimeEntity> findByMovie_NameAndScreenRoom_Cinema_NameAndShowDateAndStatusTrue(
            String movieName,
            String cinemaName,
            LocalDate date
    );


    List<ShowTimeEntity> findByScreenRoom_IdAndShowDate(Long screenRoomId, LocalDate showDate);
}
