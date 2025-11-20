package com.example.Movie_ticket_booking_service.repository;

import com.example.Movie_ticket_booking_service.entity.SeatEntity;
import com.example.Movie_ticket_booking_service.entity.ShowTimeEntity;
import com.example.Movie_ticket_booking_service.entity.TicketEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TicketRepository extends JpaRepository<TicketEntity, Long> {
    List<TicketEntity> findByShowTime_Id(Long id);
    Optional<TicketEntity> findFirstByInvoice_Id(Long id);
    int countByInvoice_Id(Long invoiceId);

    @Query("""
        select s.seatCode
        from TicketEntity t
        join t.seat s
        where t.invoice.id = :invoiceId
        order by s.seatCode asc
    """)
    List<String> findSeatCodesByInvoiceId(@Param("invoiceId") Long invoiceId);
}
