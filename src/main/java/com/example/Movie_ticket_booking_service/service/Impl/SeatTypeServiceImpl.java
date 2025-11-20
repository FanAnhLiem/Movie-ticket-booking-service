package com.example.Movie_ticket_booking_service.service.Impl;

import com.example.Movie_ticket_booking_service.dto.SeatTypeResponse;
import com.example.Movie_ticket_booking_service.dto.request.SeatTypeRequest;
import com.example.Movie_ticket_booking_service.entity.SeatTypeEntity;
import com.example.Movie_ticket_booking_service.exception.AppException;
import com.example.Movie_ticket_booking_service.exception.ErrorCode;
import com.example.Movie_ticket_booking_service.repository.SeatTypeRepository;
import com.example.Movie_ticket_booking_service.service.SeatTypeService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SeatTypeServiceImpl implements SeatTypeService {
    private final SeatTypeRepository seatTypeRepository;
    private final ModelMapper modelMapper;

    @Override
    public SeatTypeResponse createSeatType(SeatTypeRequest seatTypeRequest) {
        if(seatTypeRepository.existsByName(seatTypeRequest.getName())){
            throw new AppException(ErrorCode.SEAT_TYPE_EXISTED);
        }
        SeatTypeEntity seatTypeEntity = modelMapper.map(seatTypeRequest, SeatTypeEntity.class);
        seatTypeEntity.setStatus(true);
        try {
            return modelMapper.map(seatTypeRepository.save(seatTypeEntity), SeatTypeResponse.class);
        }catch (DataIntegrityViolationException e){
            throw new AppException(ErrorCode.DATA_VIOLATION);
        }
    }

    @Override
    public SeatTypeResponse updateSeatType(Long id, SeatTypeRequest seatTypeRequest) {
        SeatTypeEntity seatTypeEntity = seatTypeRepository.findById(id)
                .orElseThrow(()-> new AppException(ErrorCode.SEAT_TYPE_NOT_EXISTED));
        modelMapper.map(seatTypeRequest, seatTypeEntity);
        try {
            return modelMapper.map(seatTypeRepository.save(seatTypeEntity), SeatTypeResponse.class);
        }catch (DataIntegrityViolationException e){
            throw new AppException(ErrorCode.DATA_VIOLATION);
        }
    }

    @Override
    public SeatTypeResponse getSeatType(Long id) {
        SeatTypeEntity seatTypeEntity = seatTypeRepository.findById(id)
                .orElseThrow(()-> new AppException(ErrorCode.SEAT_TYPE_NOT_EXISTED));
        try {
            return modelMapper.map(seatTypeEntity, SeatTypeResponse.class);
        }catch (DataIntegrityViolationException e){
            throw new AppException(ErrorCode.DATA_VIOLATION);
        }
    }

    @Override
    public List<SeatTypeResponse> getAllSeatType() {
        List<SeatTypeEntity> entities = seatTypeRepository.findAll();
        if (entities.isEmpty()) {
            throw new AppException(ErrorCode.SEAT_TYPE_EMPTY);
        }
        return entities.stream()
                .map(entity -> modelMapper.map(entity, SeatTypeResponse.class))
                .toList();
    }

    @Override
    public boolean deleteSeatType(Long id) {
        Optional<SeatTypeEntity> optional = seatTypeRepository.findById(id);
        if (optional.isEmpty()) {
            return false;
        }

        SeatTypeEntity seatTypeEntity = optional.get();
        seatTypeEntity.setStatus(false);
        seatTypeRepository.save(seatTypeEntity);
        return true;
    }


}
