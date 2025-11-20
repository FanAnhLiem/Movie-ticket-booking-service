package com.example.Movie_ticket_booking_service.entity;

import com.example.Movie_ticket_booking_service.enums.MovieStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "movie")
public class MovieEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String description;

    private int duration;
    private String category;
    private String country;
    private String director;
    private String actors;
    private String posterUrl;
    private int ageLimit;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String trailer;
    private boolean status;

    @Column(name = "release_date")
    private LocalDate releaseDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @OneToMany(mappedBy = "movie" , cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private List<ShowTimeEntity> showTimes;

}
