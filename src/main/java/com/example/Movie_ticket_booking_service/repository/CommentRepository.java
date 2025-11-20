package com.example.Movie_ticket_booking_service.repository;

import com.example.Movie_ticket_booking_service.entity.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<CommentEntity, Long> {
}
