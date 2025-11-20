package com.example.Movie_ticket_booking_service.service.Impl;

import com.example.Movie_ticket_booking_service.dto.request.TicketPriceRequest;
import com.example.Movie_ticket_booking_service.dto.response.TicketPriceResponse;
import com.example.Movie_ticket_booking_service.entity.CinemaTypeEntity;
import com.example.Movie_ticket_booking_service.entity.ScreenRoomTypeEntity;
import com.example.Movie_ticket_booking_service.entity.SeatTypeEntity;
import com.example.Movie_ticket_booking_service.entity.TicketPriceEntity;
import com.example.Movie_ticket_booking_service.enums.TimeFrame;
import com.example.Movie_ticket_booking_service.exception.AppException;
import com.example.Movie_ticket_booking_service.exception.ErrorCode;
import com.example.Movie_ticket_booking_service.repository.CinemaTypeRepository;
import com.example.Movie_ticket_booking_service.repository.ScreenRoomTypeRepository;
import com.example.Movie_ticket_booking_service.repository.SeatTypeRepository;
import com.example.Movie_ticket_booking_service.repository.TicketPriceRepository;
import com.example.Movie_ticket_booking_service.service.TicketPriceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TicketPriceServiceImpl implements TicketPriceService {
    private final PriceService priceService;
    private final CinemaTypeRepository cinemaTypeRepository;
    private final ScreenRoomTypeRepository screenRoomTypeRepository;
    private final SeatTypeRepository seatTypeRepository;
    private final TicketPriceRepository ticketPriceRepository;

    @Override
    public TicketPriceResponse createTicketPrice(TicketPriceRequest ticketPriceRequest) {

        boolean specialDay = ticketPriceRequest.getDayType().equals("SpecialDay") ? true : false;

        if(ticketPriceRepository.existsBySpecialDayAndTimeFrameAndCinemaType_IdAndScreenRoomType_IdAndSeatType_Id(
                specialDay,
                ticketPriceRequest.getTimeFrame(),
                ticketPriceRequest.getCinemaTypeId(),
                ticketPriceRequest.getScreenRoomTypeId(),
                ticketPriceRequest.getSeatTypeId()
        )){
            throw new AppException(ErrorCode.TICKET_PRICE_EXISTED);
        }

        CinemaTypeEntity cinemaType = cinemaTypeRepository.findById(ticketPriceRequest.getCinemaTypeId())
                .orElseThrow(() -> new AppException(ErrorCode.CINEMA_TYPE_NOT_EXISTED));

        ScreenRoomTypeEntity screenRoomTypeEntity = screenRoomTypeRepository.findById(ticketPriceRequest.getScreenRoomTypeId())
                .orElseThrow(() -> new AppException(ErrorCode.SCREEN_ROOM_TYPE_NOT_EXISTED));

        SeatTypeEntity seatType = seatTypeRepository.findById(ticketPriceRequest.getSeatTypeId())
                .orElseThrow(() -> new AppException(ErrorCode.SEAT_TYPE_NOT_EXISTED));

        TimeFrame timeFrame = TimeFrame.fromLabel(ticketPriceRequest.getTimeFrame());

        Double price = priceService.finalPrice(
                cinemaType.getPriceFactor(),
                screenRoomTypeEntity.getPriceFactor(),
                seatType.getPriceFactor(),
                specialDay,
                timeFrame
        );

        TicketPriceEntity ticketPriceEntity =  new TicketPriceEntity();
        ticketPriceEntity.setCinemaType(cinemaType);
        ticketPriceEntity.setScreenRoomType(screenRoomTypeEntity);
        ticketPriceEntity.setSeatType(seatType);
        ticketPriceEntity.setSpecialDay(specialDay);
        ticketPriceEntity.setTimeFrame(ticketPriceRequest.getTimeFrame());
        ticketPriceEntity.setPrice(BigDecimal.valueOf(price));
        ticketPriceRepository.save(ticketPriceEntity);

        TicketPriceResponse ticketPriceResponse = new TicketPriceResponse();
        ticketPriceResponse.setId(ticketPriceEntity.getId());
        ticketPriceResponse.setPrice(ticketPriceEntity.getPrice());
        ticketPriceResponse.setTimeFrame(ticketPriceEntity.getTimeFrame());
        ticketPriceResponse.setDayType(ticketPriceEntity.isSpecialDay() ? "Ngày lễ, cuối tuần" : "Ngày thường");
        ticketPriceResponse.setCinemaType(ticketPriceEntity.getCinemaType().getName());
        ticketPriceResponse.setScreenRoomType(ticketPriceEntity.getScreenRoomType().getName());
        ticketPriceResponse.setSeatType(ticketPriceEntity.getSeatType().getName());
        return ticketPriceResponse;
    }

    @Override
    public TicketPriceEntity getSeatTicketPrice(Long cinemaTypeId,
                                                Long screenRoomTypeId,
                                                Long seatTypeId,
                                                LocalDate date,
                                                LocalTime time) {
        boolean specialday = false;
        if(priceService.isSpecialDay(date) || priceService.isWeekend(date)){
            specialday = true;
        }

        TimeFrame timeFrame = TimeFrame.from(time);
        String frame = timeFrame.getTimeFrame();

        TicketPriceEntity ticketPriceEntity = ticketPriceRepository
                .findBySpecialDayAndTimeFrameAndCinemaType_IdAndScreenRoomType_IdAndSeatType_Id(
                        specialday,
                        frame,
                        cinemaTypeId,
                        screenRoomTypeId,
                        seatTypeId
                ).orElseThrow(() -> new AppException(ErrorCode.TICKET_PRICE_NOT_EXISTED));

        return ticketPriceEntity;
    }

    @Override
    public TicketPriceResponse updateTicketPrice(Long id, TicketPriceRequest ticketPriceRequest) {
        boolean specialDay = ticketPriceRequest.getDayType().equals("SpecialDay") ? true : false;

        TicketPriceEntity ticketPriceEntity = ticketPriceRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.TICKET_PRICE_NOT_EXISTED));

        if(ticketPriceRepository.existsBySpecialDayAndTimeFrameAndCinemaType_IdAndScreenRoomType_IdAndSeatType_Id(
                specialDay,
                ticketPriceRequest.getTimeFrame(),
                ticketPriceRequest.getCinemaTypeId(),
                ticketPriceRequest.getScreenRoomTypeId(),
                ticketPriceRequest.getSeatTypeId()
        )){
            throw new AppException(ErrorCode.TICKET_PRICE_EXISTED);
        }
        CinemaTypeEntity cinemaType = cinemaTypeRepository.findById(ticketPriceRequest.getCinemaTypeId())
                .orElseThrow(() -> new AppException(ErrorCode.CINEMA_TYPE_NOT_EXISTED));

        ScreenRoomTypeEntity screenRoomTypeEntity = screenRoomTypeRepository.findById(ticketPriceRequest.getScreenRoomTypeId())
                .orElseThrow(() -> new AppException(ErrorCode.SCREEN_ROOM_TYPE_NOT_EXISTED));

        SeatTypeEntity seatType = seatTypeRepository.findById(ticketPriceRequest.getSeatTypeId())
                .orElseThrow(() -> new AppException(ErrorCode.SEAT_TYPE_NOT_EXISTED));

        TimeFrame timeFrame = TimeFrame.fromLabel(ticketPriceRequest.getTimeFrame());

        Double price = priceService.finalPrice(
                cinemaType.getPriceFactor(),
                screenRoomTypeEntity.getPriceFactor(),
                seatType.getPriceFactor(),
                specialDay,
                timeFrame
        );

        ticketPriceEntity.setCinemaType(cinemaType);
        ticketPriceEntity.setScreenRoomType(screenRoomTypeEntity);
        ticketPriceEntity.setSeatType(seatType);
        ticketPriceEntity.setSpecialDay(specialDay);
        ticketPriceEntity.setTimeFrame(ticketPriceRequest.getTimeFrame());
        ticketPriceEntity.setPrice(BigDecimal.valueOf(price));
        ticketPriceRepository.save(ticketPriceEntity);

        TicketPriceResponse ticketPriceResponse = new TicketPriceResponse();
        ticketPriceResponse.setId(ticketPriceEntity.getId());
        ticketPriceResponse.setPrice(ticketPriceEntity.getPrice());
        ticketPriceResponse.setTimeFrame(ticketPriceEntity.getTimeFrame());
        ticketPriceResponse.setDayType(ticketPriceEntity.isSpecialDay() ? "Ngày lễ, cuối tuần" : "Ngày thường");
        ticketPriceResponse.setCinemaType(ticketPriceEntity.getCinemaType().getName());
        ticketPriceResponse.setScreenRoomType(ticketPriceEntity.getScreenRoomType().getName());
        ticketPriceResponse.setSeatType(ticketPriceEntity.getSeatType().getName());
        return ticketPriceResponse;
    }

    @Override
    public List<TicketPriceResponse> getAllTicketPrice() {
        List<TicketPriceEntity> ticketPriceEntities = ticketPriceRepository.findAll();
        return ticketPriceEntities.stream()
                .map(tp -> {
                    return TicketPriceResponse.builder()
                            .id(tp.getId())
                            .price(tp.getPrice())
                            .timeFrame(tp.getTimeFrame())
                            .dayType(tp.isSpecialDay() ? "Ngày lễ, cuối tuần" : "Ngày thường")
                            .cinemaType(tp.getCinemaType().getName())
                            .screenRoomType(tp.getScreenRoomType().getName())
                            .seatType(tp.getSeatType().getName())
                            .build();
                }).collect(Collectors.toList());
    }


}
