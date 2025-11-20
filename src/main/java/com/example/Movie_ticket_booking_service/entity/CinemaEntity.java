package com.example.Movie_ticket_booking_service.entity;

import jakarta.annotation.Generated;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "cinema")
public class CinemaEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String address;
    private Boolean status;

    @OneToMany(mappedBy = "cinema" , cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private List<ScreenRoomEntity> screenRooms;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cinematype_id")
    private CinemaTypeEntity cinemaType;
}
