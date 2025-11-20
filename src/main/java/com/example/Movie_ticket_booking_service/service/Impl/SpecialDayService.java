package com.example.Movie_ticket_booking_service.service.Impl;

import com.example.Movie_ticket_booking_service.dto.request.SpecialDayRequest;
import com.example.Movie_ticket_booking_service.entity.SpecialDayEntity;
import com.example.Movie_ticket_booking_service.exception.AppException;
import com.example.Movie_ticket_booking_service.exception.ErrorCode;
import com.example.Movie_ticket_booking_service.repository.SpecialDayRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@RequiredArgsConstructor
@Service
public class SpecialDayService {
    private final SpecialDayRepository specialDayRepository;
    private final ModelMapper modelMapper;

    public SpecialDayEntity createSpecialDay(@Valid @RequestBody SpecialDayRequest specialDayRequest) {
        if(specialDayRepository.existsByMonthAndDay(specialDayRequest.getMonth(), specialDayRequest.getDay())){
            throw new AppException(ErrorCode.SPECIAL_DAY_EXISTED);
        }
        try {
            return specialDayRepository.save(modelMapper.map(specialDayRequest, SpecialDayEntity.class));
        }catch (DataIntegrityViolationException e){
            throw new AppException(ErrorCode.DATA_VIOLATION);
        }
    }

    public List<SpecialDayEntity> getAllSpecialDay() {
        return specialDayRepository.findAll();
    }

    public SpecialDayEntity getSpecialDay(Long id) {
        SpecialDayEntity specialDay = specialDayRepository.findById(id)
                .orElseThrow(()-> new AppException(ErrorCode.SPECIAL_DAY_NOT_EXISTED));
        return specialDay;
    }

    public void deleteSpecialDay(Long id) {
        if(!specialDayRepository.existsById(id)){
            throw new AppException(ErrorCode.SPECIAL_DAY_NOT_EXISTED);
        }
        specialDayRepository.deleteById(id);
    }

    public SpecialDayEntity updateSpecialDay(Long id, SpecialDayRequest specialDayRequest) {
        SpecialDayEntity specialDay = specialDayRepository.findById(id)
                .orElseThrow(()-> new AppException(ErrorCode.SPECIAL_DAY_NOT_EXISTED));
        try {
            modelMapper.map(specialDayRequest, specialDay);
            return specialDayRepository.save(specialDay);
        }catch (DataIntegrityViolationException e){
            throw new AppException(ErrorCode.DATA_VIOLATION);
        }
    }
}
