package com.example.Movie_ticket_booking_service.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Where;

@Getter
@Setter
@Entity
@Table(name = "seat")
public class SeatEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "seat_row")   // tránh từ khóa ROW
    private int row;

    @Column(name = "seat_col")   // tránh từ khóa COLUMN
    private int column;
    private boolean isActive;
    private String seatCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seattype_id")
    private SeatTypeEntity seatType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "screenroom_id")
    private ScreenRoomEntity screenRoom;

}
