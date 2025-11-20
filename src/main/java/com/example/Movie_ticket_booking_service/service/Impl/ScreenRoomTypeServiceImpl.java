package com.example.Movie_ticket_booking_service.service.Impl;

import com.example.Movie_ticket_booking_service.dto.request.ScreenRoomTypeRequest;
import com.example.Movie_ticket_booking_service.dto.response.ScreenRoomResponse;
import com.example.Movie_ticket_booking_service.dto.response.ScreenRoomTypeResponse;
import com.example.Movie_ticket_booking_service.entity.ScreenRoomEntity;
import com.example.Movie_ticket_booking_service.entity.ScreenRoomTypeEntity;
import com.example.Movie_ticket_booking_service.exception.AppException;
import com.example.Movie_ticket_booking_service.exception.ErrorCode;
import com.example.Movie_ticket_booking_service.repository.ScreenRoomTypeRepository;
import com.example.Movie_ticket_booking_service.service.ScreenRoomTypeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ScreenRoomTypeServiceImpl implements ScreenRoomTypeService {
    private final ScreenRoomTypeRepository screenRoomTypeRepository;
    private final ModelMapper modelMapper;

    @Override
    public ScreenRoomTypeResponse createScreenRoomType(ScreenRoomTypeRequest screenRoomTypeRequest) {
        if(screenRoomTypeRepository.existsByName(screenRoomTypeRequest.getName())){
            throw new AppException(ErrorCode.SCREEN_ROOM_TYPE_EXISTED);
        }
        ScreenRoomTypeEntity screenRoomTypeEntity = modelMapper.map(screenRoomTypeRequest, ScreenRoomTypeEntity.class);
        screenRoomTypeEntity.setStatus(true);
        try {
            return modelMapper.map(screenRoomTypeRepository.save(screenRoomTypeEntity), ScreenRoomTypeResponse.class);
        }catch (DataIntegrityViolationException e){
            throw new AppException(ErrorCode.DATA_VIOLATION);
        }
    }

    @Override
    public ScreenRoomTypeResponse updateScreenRoomType(Long id, ScreenRoomTypeRequest screenRoomTypeRequest) {
        ScreenRoomTypeEntity screenRoomTypeEntity = screenRoomTypeRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.SCREEN_ROOM_TYPE_NOT_EXISTED));
        modelMapper.map(screenRoomTypeRequest, screenRoomTypeEntity);
        return modelMapper.map(screenRoomTypeRepository.save(screenRoomTypeEntity), ScreenRoomTypeResponse.class);
    }

    @Override
    public ScreenRoomTypeResponse getScreenRoomType(Long id) {
        ScreenRoomTypeEntity screenRoomTypeEntity = screenRoomTypeRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.SCREEN_ROOM_TYPE_NOT_EXISTED));
        return modelMapper.map(screenRoomTypeEntity, ScreenRoomTypeResponse.class);
    }

    @Override
    public List<ScreenRoomTypeResponse> getAllScreenRoomType() {
        List<ScreenRoomTypeEntity> screenRoomTypeEntities = screenRoomTypeRepository.findAll();
        List<ScreenRoomTypeResponse> screenRoomTypeResponses = screenRoomTypeEntities.stream()
                .map(s -> modelMapper.map(s, ScreenRoomTypeResponse.class))
                .toList();
        return screenRoomTypeResponses;
    }

    @Override
    public boolean deleteScreenRoomType(Long id) {
        Optional<ScreenRoomTypeEntity> optional = screenRoomTypeRepository.findById(id);
        if (optional.isEmpty()) {
            return false;
        }

        ScreenRoomTypeEntity screenRoomTypeEntity = optional.get();
        screenRoomTypeEntity.setStatus(false);
        screenRoomTypeRepository.save(screenRoomTypeEntity);
        return true;
    }
}
