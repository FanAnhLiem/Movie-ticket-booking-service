package com.example.Movie_ticket_booking_service.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Where;

import java.util.List;
@Getter
@Setter
@Entity
@Table(name = "seat_type")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SeatTypeEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private Double priceFactor;
    private boolean status;

    @OneToMany(mappedBy = "seatType" , cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private List<SeatEntity> seats;
}
