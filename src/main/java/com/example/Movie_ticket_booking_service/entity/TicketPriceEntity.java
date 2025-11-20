package com.example.Movie_ticket_booking_service.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Table(name = "ticket_price")
@Data
public class TicketPriceEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private BigDecimal price;
    private boolean specialDay;
    private String timeFrame;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cinematype_id")
    private CinemaTypeEntity cinemaType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "screenroomtype_id")
    private ScreenRoomTypeEntity screenRoomType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seattype_id")
    private SeatTypeEntity seatType;


}
