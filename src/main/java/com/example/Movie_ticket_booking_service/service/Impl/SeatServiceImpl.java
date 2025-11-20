package com.example.Movie_ticket_booking_service.service.Impl;

import com.example.Movie_ticket_booking_service.dto.request.SeatRequest;
import com.example.Movie_ticket_booking_service.dto.response.SeatResponse;
import com.example.Movie_ticket_booking_service.dto.response.SeatShowTime;
import com.example.Movie_ticket_booking_service.dto.response.SeatShowTimeResponse;
import com.example.Movie_ticket_booking_service.dto.response.SeatSummaryRepo;
import com.example.Movie_ticket_booking_service.entity.*;
import com.example.Movie_ticket_booking_service.enums.SeatStatus;
import com.example.Movie_ticket_booking_service.exception.AppException;
import com.example.Movie_ticket_booking_service.exception.ErrorCode;
import com.example.Movie_ticket_booking_service.repository.*;
import com.example.Movie_ticket_booking_service.service.SeatService;
import com.example.Movie_ticket_booking_service.service.TicketPriceService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SeatServiceImpl implements SeatService {
    private final SeatRepository seatRepository;
    private final ScreenRoomRepository screenRoomRepository;
    private final SeatTypeRepository seatTypeRepository;
    private final ModelMapper modelMapper;
    private final ShowTimeRepository showTimeRepository;
    private final TicketRepository ticketRepository;
    private final PriceService priceService;
    private final RedisService redisService;
    private final TicketPriceService ticketPriceService;

    @Override
    public List<SeatResponse> createSeats(SeatRequest seatRequest) {
        Long[][] matrix = seatRequest.getSeatTypeIds();

        // Lấy phòng
        ScreenRoomEntity screenRoom = screenRoomRepository.findById(seatRequest.getScreenRoomId())
                .orElseThrow(() -> new AppException(ErrorCode.SCREEN_ROOM_NOT_EXISTED));

        // Gom tất cả seatTypeId sẽ dùng để bulk fetch
        Set<Long> seatTypeIds = new HashSet<>();
        for (int i = 0; i < seatRequest.getRows(); i++) {
            for (int j = 0; j < seatRequest.getColumns(); j++) {
                Long id = matrix[i][j];
                if (id != null && id != 0L)
                    seatTypeIds.add(id);
            }
        }

        // Nếu không có ghế hợp lệ, trả rỗng
        if (seatTypeIds.isEmpty()) {
            return Collections.emptyList();
        }

        // Bulk fetch seat types
        Map<Long, SeatTypeEntity> seatTypeMap = seatTypeRepository.findAllById(seatTypeIds)
                .stream().collect(Collectors.toMap(SeatTypeEntity::getId, st -> st));

        // Kiểm tra seat type nào không tồn tại
        if (seatTypeMap.size() != seatTypeIds.size()) {
            // tìm id thiếu
            Set<Long> missing = new HashSet<>(seatTypeIds);
            missing.removeAll(seatTypeMap.keySet());
            throw new AppException(ErrorCode.SEAT_TYPE_NOT_EXISTED);
        }

        // Tạo ghế
        List<SeatEntity> toSave = new ArrayList<>();
        for (int i = 0; i < seatRequest.getRows(); i++) {
            for (int j = 0; j < seatRequest.getColumns(); j++) {
                Long seatTypeId = matrix[i][j];

                if (seatTypeId == null || seatTypeId == 0L) continue;

                SeatTypeEntity seatType = seatTypeMap.get(seatTypeId);
                SeatEntity seat = new SeatEntity();
                seat.setSeatCode(genSeatCode(i, j));
                seat.setSeatType(seatType);
                seat.setRow(i);
                seat.setColumn(j);
                seat.setScreenRoom(screenRoom);
                seat.setActive(true);
                toSave.add(seat);
            }
        }

        return seatRepository.saveAll(toSave).stream()
                .map(s -> SeatResponse.builder()
                        .id(s.getId())
                        .seatCode(s.getSeatCode())
                        .seatTypeId(s.getSeatType().getId())
                        .row(s.getRow())
                        .column(s.getColumn())
                        .screenRoomId(s.getScreenRoom().getId())
                        .build())
                .toList();
    }



    String genSeatCode(int row, int column){
        char code = (char)('A' + row);
        return code + String.valueOf(column+1);
    }

    @Override
    public SeatShowTime getListSeat(Long showTimeId) {
        ShowTimeEntity showTime = showTimeRepository.findById(showTimeId)
                .orElseThrow(()-> new AppException(ErrorCode.SHOW_TIME_NOT_EXISTED));
        Long screenRoomId = showTime.getScreenRoom().getId();
        List<SeatEntity> seatEntityList = seatRepository.findByScreenRoom_Id(screenRoomId);
        List<SeatEntity> seatEntityListBooked = ticketRepository.findByShowTime_Id(showTimeId).stream()
                .map(tk -> tk.getSeat())
                .toList();
        List<SeatShowTimeResponse> seatList = seatEntityList.stream().map(
                seat ->{
                    SeatShowTimeResponse seatDto = new SeatShowTimeResponse();
                    TicketPriceEntity ticketPrice = ticketPriceService.getSeatTicketPrice(
                            showTime.getScreenRoom().getCinema().getCinemaType().getId(),
                            showTime.getScreenRoom().getScreenRoomType().getId(),
                            seat.getSeatType().getId(),
                            showTime.getShowDate(),
                            showTime.getStartTime()
                    );
                    seatDto.setPrice(ticketPrice.getPrice());
                    seatDto.setSeatCode(seat.getSeatCode());
                    seatDto.setSeatTypeName(seat.getSeatType().getName());
                    if(seatEntityListBooked.stream()
                            .map(seatBooked -> seatBooked.getId())
                            .collect(Collectors.toList()).contains(seat.getId())){
                        seatDto.setStatus(SeatStatus.BOOKED);
                    }
                    if(redisService.isSeatHoldingList(showTimeId, seat.getId())){
                        seatDto.setStatus(SeatStatus.HOLDING);
                    }
                    modelMapper.map(seat, seatDto);
                    return seatDto;
                }).collect(Collectors.toList());

        SeatShowTime seatShowTime = new SeatShowTime();
        seatShowTime.setSeatList(seatList);
        seatShowTime.setSumSeats(seatRepository.countByScreenRoom_Id(showTimeId));
        seatShowTime.setShowTimeId(showTime.getId());
        seatShowTime.setMovieName(showTime.getMovie().getName());
        seatShowTime.setCinemaName(showTime.getScreenRoom().getCinema().getName());

        return seatShowTime;
    }

    @Override
    public List<SeatSummaryRepo> getSeatListByScreenRoom(long showRoomId) {
        List<SeatEntity> seatEntityList = seatRepository.findByScreenRoom_Id(showRoomId);
        List<SeatSummaryRepo> seatList = seatEntityList.stream()
                .map(s-> {
                    SeatSummaryRepo seatRepo = SeatSummaryRepo.builder()
                            .id(s.getId())
                            .seatTypeName(s.getSeatType().getName())
                            .seatTypeId(s.getSeatType().getId())
                            .seatCode(s.getSeatCode())
                            .build();
                    return seatRepo;
                })
                .collect(Collectors.toList());
        return seatList;
    }

    @Override
    public void deleteSeat(Long id) {
        SeatEntity seat = seatRepository.findById(id).orElseThrow(()-> new AppException(ErrorCode.SEAT_NOT_EXISTED));
        seat.setActive(false);
        seatRepository.save(seat);
    }
}
