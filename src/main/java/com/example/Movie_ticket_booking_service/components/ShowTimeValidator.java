package com.example.Movie_ticket_booking_service.components;

import com.example.Movie_ticket_booking_service.dto.request.ShowTimeRequest;
import com.example.Movie_ticket_booking_service.exception.AppException;
import com.example.Movie_ticket_booking_service.exception.ErrorCode;
import com.example.Movie_ticket_booking_service.repository.ShowTimeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ShowTimeValidator {
    private final ShowTimeRepository showTimeRepository;

    public void checkOverlap(ShowTimeRequest showTimeRequest, Long showTimeId){
        boolean overlap = showTimeRepository.existsOverlapping(
                showTimeRequest.getScreenRoomId(),
                showTimeRequest.getShowDate(),
                showTimeRequest.getStartTime(),
                showTimeRequest.getEndTime(),
                showTimeId
        );
        if(overlap){
            throw new AppException(ErrorCode.SHOW_TIME_OVERLAP);
        }
    }
}
