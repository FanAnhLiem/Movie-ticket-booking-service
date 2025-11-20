package com.example.Movie_ticket_booking_service.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Where;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "screen_room")
public class ScreenRoomEntity extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private Boolean status;

    @OneToMany(mappedBy = "screenRoom", cascade = {CascadeType.MERGE, CascadeType.PERSIST}, fetch = FetchType.LAZY)
    private List<ShowTimeEntity> showTimes;

    @OneToMany(mappedBy = "screenRoom", cascade = {CascadeType.MERGE, CascadeType.PERSIST}, fetch = FetchType.LAZY)
    private List<SeatEntity> seats;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cinema_id")
    private CinemaEntity cinema;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "screenroomtype_id")
    private ScreenRoomTypeEntity screenRoomType;

}
