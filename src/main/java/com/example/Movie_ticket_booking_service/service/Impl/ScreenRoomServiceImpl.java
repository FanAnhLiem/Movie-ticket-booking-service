package com.example.Movie_ticket_booking_service.service.Impl;

import com.example.Movie_ticket_booking_service.dto.request.ScreenRoomRequest;
import com.example.Movie_ticket_booking_service.dto.response.ScreenRoomDetailResponse;
import com.example.Movie_ticket_booking_service.dto.response.ScreenRoomResponse;
import com.example.Movie_ticket_booking_service.entity.ScreenRoomEntity;
import com.example.Movie_ticket_booking_service.entity.ScreenRoomTypeEntity;
import com.example.Movie_ticket_booking_service.exception.AppException;
import com.example.Movie_ticket_booking_service.exception.ErrorCode;
import com.example.Movie_ticket_booking_service.repository.CinemaRepository;
import com.example.Movie_ticket_booking_service.repository.ScreenRoomRepository;
import com.example.Movie_ticket_booking_service.repository.ScreenRoomTypeRepository;
import com.example.Movie_ticket_booking_service.service.ScreenRoomService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ScreenRoomServiceImpl implements ScreenRoomService {
    private final ScreenRoomRepository screenRoomRepository;
    private final ScreenRoomTypeRepository screenRoomTypeRepository;
    private final ModelMapper modelMapper;
    private final CinemaRepository cinemaRepository;
    @Override
    public ScreenRoomResponse createScreenRoom(ScreenRoomRequest screenRoomRequest) {
        if(screenRoomRepository.existsByNameAndCinema_IdAndScreenRoomType_Id(screenRoomRequest.getName(),
                screenRoomRequest.getCinemaId(),
                screenRoomRequest.getScreenRoomTypeId())){
            throw new AppException(ErrorCode.SCREEN_ROOM_EXISTED);
        }
        ScreenRoomEntity screenRoomEntity = modelMapper.map(screenRoomRequest, ScreenRoomEntity.class);
        screenRoomEntity.setScreenRoomType(screenRoomTypeRepository.findById(screenRoomRequest.getScreenRoomTypeId()).get());
        screenRoomEntity.setCinema(cinemaRepository.findById(screenRoomRequest.getCinemaId()).get());
        screenRoomEntity.setStatus(true);
        try {
            ScreenRoomEntity screenRoom = screenRoomRepository.save(screenRoomEntity);
            ScreenRoomResponse screenRoomResponse = modelMapper.map(screenRoom, ScreenRoomResponse.class);
            screenRoomResponse.setScreenRoomTypeId(screenRoom.getScreenRoomType().getId());
            screenRoomResponse.setCinemaId(screenRoom.getCinema().getId());
            return screenRoomResponse;

        }catch (DataIntegrityViolationException e){
            throw new AppException(ErrorCode.DATA_VIOLATION);
        }
    }

    @Override
    public ScreenRoomResponse updateScreenRoom(Long id, ScreenRoomRequest screenRoomRequest) {
        ScreenRoomEntity screenRoomEntity = screenRoomRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.SCREEN_ROOM_NOT_EXISTED));
        if(screenRoomRepository.existsByNameAndCinema_IdAndScreenRoomType_Id(screenRoomRequest.getName(),
                screenRoomRequest.getCinemaId(),
                screenRoomRequest.getScreenRoomTypeId())) {
            throw new AppException(ErrorCode.SCREEN_ROOM_EXISTED);
        }
        modelMapper.map(screenRoomRequest, screenRoomEntity);
        screenRoomEntity.setScreenRoomType(screenRoomTypeRepository.findById(screenRoomRequest.getScreenRoomTypeId()).get());
        screenRoomEntity.setCinema(cinemaRepository.findById(screenRoomRequest.getCinemaId()).get());
        try {
            ScreenRoomEntity screenRoom = screenRoomRepository.save(screenRoomEntity);
            ScreenRoomResponse screenRoomResponse = modelMapper.map(screenRoom, ScreenRoomResponse.class);
            screenRoomResponse.setScreenRoomTypeId(screenRoom.getScreenRoomType().getId());
            screenRoomResponse.setCinemaId(screenRoom.getCinema().getId());
            return screenRoomResponse;

        }catch (DataIntegrityViolationException e){
            throw new AppException(ErrorCode.DATA_VIOLATION);
        }
    }

    @Override
    public List<ScreenRoomDetailResponse> getScreenRoomList(Long id) {
        List<ScreenRoomEntity> screenRoomEntityList = screenRoomRepository.findByCinemaId(id);
        List<ScreenRoomDetailResponse> screenRoomDetailResponseList = screenRoomEntityList.stream()
                .map(sr -> {
                    ScreenRoomDetailResponse screenRoomDetailResponse = ScreenRoomDetailResponse.builder()
                            .id(sr.getId())
                            .name(sr.getName())
                            .roomType(sr.getScreenRoomType().getName())
                            .status(Boolean.TRUE.equals(sr.getStatus()) ? "Đang hoạt động" : "Bảo trì")
                            .build();
                    return screenRoomDetailResponse;
                })
                .collect(Collectors.toList());
        return screenRoomDetailResponseList;
    }

    @Override
    public ScreenRoomResponse getScreenRoomDetail(Long id) {
        ScreenRoomEntity screenRoom = screenRoomRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.SCREEN_ROOM_NOT_EXISTED));
        ScreenRoomResponse screenRoomResponse = modelMapper.map(screenRoom, ScreenRoomResponse.class);
        screenRoomResponse.setScreenRoomTypeId(screenRoom.getScreenRoomType().getId());
        screenRoomResponse.setCinemaId(screenRoom.getCinema().getId());
        return screenRoomResponse;
    }

    @Override
    public boolean deleteScreenRoom(Long id) {
        Optional<ScreenRoomEntity> optional = screenRoomRepository.findById(id);
        if (optional.isEmpty()) {
            return false;
        }

        ScreenRoomEntity screenRoomEntity = optional.get();
        screenRoomEntity.setStatus(false);
        screenRoomRepository.save(screenRoomEntity);
        return true;
    }

    @Override
    public List<ScreenRoomDetailResponse> getScreenRoomActiveList(Long id) {
        List<ScreenRoomEntity> screenRoomEntityList = screenRoomRepository.findByCinemaIdAndStatusTrue(id);
        List<ScreenRoomDetailResponse> screenRoomDetailResponseList = screenRoomEntityList.stream()
                .map(sr -> {
                    ScreenRoomDetailResponse screenRoomDetailResponse = ScreenRoomDetailResponse.builder()
                            .id(sr.getId())
                            .name(sr.getName())
                            .roomType(sr.getScreenRoomType().getName())
                            .status(Boolean.TRUE.equals(sr.getStatus()) ? "Đang hoạt động" : "Bảo trì")
                            .build();
                    return screenRoomDetailResponse;
                })
                .collect(Collectors.toList());
        return screenRoomDetailResponseList;
    }

}
