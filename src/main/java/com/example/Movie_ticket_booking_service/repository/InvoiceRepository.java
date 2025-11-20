package com.example.Movie_ticket_booking_service.repository;

import com.example.Movie_ticket_booking_service.entity.InvoiceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface InvoiceRepository extends JpaRepository<InvoiceEntity, Long> {

    Optional<InvoiceEntity> findByTxnRef(String txnRef);

    List<InvoiceEntity> findByUser_Id(Long userId);

    List<InvoiceEntity> findByCreateDateBetween(LocalDate startDate, LocalDate endDate);

    Optional<InvoiceEntity> findByBookingCode(String bookingCode);

    @Query("""
        SELECT i FROM InvoiceEntity i
        WHERE MONTH(i.createDate) = :month
          AND YEAR(i.createDate)  = :year
    """)
    List<InvoiceEntity> findAllByMonthAndYear(
            @Param("month") int month,
            @Param("year") int year
    );

    @Query("""
        SELECT i FROM InvoiceEntity i
        WHERE YEAR(i.createDate)  = :year
    """)
    List<InvoiceEntity> findAllByYear(
            @Param("year") int year
    );
}
