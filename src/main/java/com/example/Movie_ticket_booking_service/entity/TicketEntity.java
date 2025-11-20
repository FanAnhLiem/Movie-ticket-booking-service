package com.example.Movie_ticket_booking_service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ticket")
public class TicketEntity extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seat_id")
    private SeatEntity seat;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "showTime_id")
    private ShowTimeEntity showTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ticketprice_id")
    private TicketPriceEntity ticketPrice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "invoice_id")
    private InvoiceEntity invoice;

}
