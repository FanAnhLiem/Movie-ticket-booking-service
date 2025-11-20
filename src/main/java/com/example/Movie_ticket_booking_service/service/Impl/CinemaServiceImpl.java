package com.example.Movie_ticket_booking_service.service.Impl;

import com.example.Movie_ticket_booking_service.components.RagDocumentMapper;
import com.example.Movie_ticket_booking_service.dto.request.CinemaRequest;
import com.example.Movie_ticket_booking_service.dto.response.*;
import com.example.Movie_ticket_booking_service.entity.CinemaEntity;
import com.example.Movie_ticket_booking_service.entity.CinemaTypeEntity;
import com.example.Movie_ticket_booking_service.exception.AppException;
import com.example.Movie_ticket_booking_service.exception.ErrorCode;
import com.example.Movie_ticket_booking_service.repository.CinemaRepository;
import com.example.Movie_ticket_booking_service.repository.CinemaTypeRepository;
import com.example.Movie_ticket_booking_service.repository.ScreenRoomRepository;
import com.example.Movie_ticket_booking_service.service.CinemaService;
import com.example.Movie_ticket_booking_service.service.chatbotServices.RagIngestionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CinemaServiceImpl implements CinemaService {
    private final CinemaRepository cinemaRepository;
    private final ModelMapper modelMapper;
    private final ScreenRoomRepository screenRoomRepository;
    private final CinemaTypeRepository cinemaTypeRepository;
    private final RagDocumentMapper ragDocumentMapper;
    private final VectorStore vectorStore;

    @Override
    public CinemaResponse createCinema(CinemaRequest cinemaRequest) {
        if (cinemaRepository.existsByName(cinemaRequest.getName())) {
            throw new AppException(ErrorCode.CINEMA_EXISTED);
        }
        CinemaTypeEntity cinemaType = cinemaTypeRepository.findById(cinemaRequest.getCinemaTypeId()).
                orElseThrow(() -> new AppException(ErrorCode.CINEMA_TYPE_NOT_EXISTED));
        CinemaEntity cinemaEntity = modelMapper.map(cinemaRequest, CinemaEntity.class);
        cinemaEntity.setCinemaType(cinemaType);
        cinemaEntity.setStatus(true);
        try {
            CinemaEntity saved = cinemaRepository.save(cinemaEntity);
            Document cinemaDocument = ragDocumentMapper.fromCinema(saved);
            vectorStore.add(List.of(cinemaDocument));
            CinemaResponse cinemaResponse = modelMapper.map(saved, CinemaResponse.class);
            cinemaResponse.setCinemaTypeName(cinemaType.getName());
            cinemaResponse.setStatus(Boolean.TRUE.equals(cinemaEntity.getStatus()) ? "Đang hoạt đông" : "Tạm ngừng hoạt động");
            return cinemaResponse;
        } catch (DataIntegrityViolationException e) {
            throw new AppException(ErrorCode.DATA_VIOLATION);
        }
    }

    @Override
    public CinemaResponse updateCinema(Long id, CinemaRequest cinemaRequest) {
        CinemaEntity cinemaEntity = cinemaRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.CINEMA_NOT_EXISTED));
        CinemaTypeEntity cinemaType = cinemaTypeRepository.findById(cinemaRequest.getCinemaTypeId()).
                orElseThrow(() -> new AppException(ErrorCode.CINEMA_TYPE_NOT_EXISTED));
        modelMapper.map(cinemaRequest, cinemaEntity);
        cinemaEntity.setCinemaType(cinemaType);
        cinemaEntity.setStatus(true);
        try {
            CinemaResponse cinemaResponse = modelMapper.map(cinemaRepository.save(cinemaEntity), CinemaResponse.class);
            cinemaResponse.setCinemaTypeName(cinemaType.getName());
            cinemaResponse.setStatus(Boolean.TRUE.equals(cinemaEntity.getStatus()) ? "Đang hoạt đông" : "Tạm ngừng hoạt động");
            return  cinemaResponse;
        }catch (DataIntegrityViolationException e) {
            throw new AppException(ErrorCode.DATA_VIOLATION);
        }
    }


    @Override
    public List<CinemaSummaryResponse> getCinemas(String address) {
        List<CinemaEntity> cinemaEntities = cinemaRepository.findByAddressAndStatusTrue(address);
        List<CinemaSummaryResponse> cinemaSummaryResponseList = cinemaEntities.stream()
                .map(c -> modelMapper.map(c, CinemaSummaryResponse.class))
                .toList();
        return cinemaSummaryResponseList;
    }

    @Override
    public CinemaResponse getCinema(Long id) {
        CinemaEntity cinemaEntity = cinemaRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.CINEMA_NOT_EXISTED));
        try {
            CinemaResponse cinemaResponse = modelMapper.map(cinemaEntity, CinemaResponse.class);
            cinemaResponse.setCinemaTypeName(cinemaEntity.getCinemaType().getName());
            cinemaResponse.setStatus(Boolean.TRUE.equals(cinemaEntity.getStatus()) ? "Đang hoạt đông" : "Tạm ngừng hoạt động");
            return cinemaResponse;
        }catch (DataIntegrityViolationException e) {
            throw new AppException(ErrorCode.DATA_VIOLATION);
        }
    }

    @Override
    public List<CinemaResponse> getListCinemas() {
        List<CinemaEntity> cinemaEntities = cinemaRepository.findAll();

        return cinemaEntities.stream()
                .map(c-> CinemaResponse.builder()
                        .id(c.getId())
                        .name(c.getName())
                        .address(c.getAddress())
                        .cinemaTypeName(c.getCinemaType().getName())
                        .status(Boolean.TRUE.equals(c.getStatus()) ? "đang hoạt động" : "Tạm ngừng hoạt động")
                        .build())
                .collect(Collectors.toList());
    }


    @Override
    public void deleteCinema(Long id) {
        CinemaEntity cinemaEntity = cinemaRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.CINEMA_NOT_EXISTED));
        cinemaEntity.setStatus(false);
        cinemaRepository.save(cinemaEntity);
    }

    @Override
    public List<CinemaTypeResponse> getCinemaTypes() {
        return cinemaTypeRepository.findAll().stream()
                .map(ct -> modelMapper.map(ct, CinemaTypeResponse.class))
                .collect(Collectors.toList());
    }
}
