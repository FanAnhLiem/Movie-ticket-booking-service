package com.example.Movie_ticket_booking_service.service.Impl;

import com.example.Movie_ticket_booking_service.dto.TxnInfo;
import com.example.Movie_ticket_booking_service.entity.*;
import com.example.Movie_ticket_booking_service.exception.AppException;
import com.example.Movie_ticket_booking_service.exception.ErrorCode;
import com.example.Movie_ticket_booking_service.repository.*;
import com.example.Movie_ticket_booking_service.service.TicketPriceService;
import com.example.Movie_ticket_booking_service.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class TicketServiceImpl implements TicketService {
    private final InvoiceRepository invoiceRepository;
    private final PriceService priceService;
    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;
    private final RedisService redisService;
    private final ShowTimeRepository showTimeEntityRepository;
    private final SeatRepository seatRepository;
    private final TicketPriceService ticketPriceService;



    @Override
    public void createTicket(String vnp_TxRef, InvoiceEntity invoiceEntity) {
        TxnInfo txnInfo = redisService.getByTxnRef(vnp_TxRef);
        ShowTimeEntity showTime = showTimeEntityRepository.findById(txnInfo.getShowTimeId())
                .orElseThrow(() -> new AppException(ErrorCode.SHOW_TIME_NOT_EXISTED));

        Long cinemaType = showTime.getScreenRoom().getCinema().getCinemaType().getId();
        Long screenRoomType = showTime.getScreenRoom().getScreenRoomType().getId();
        LocalDate date = showTime.getShowDate();
        LocalTime time = showTime.getStartTime();

        Map<Long, TicketPriceEntity> ticketPriceMap = new HashMap<>();

        List<SeatEntity> seatEntities = seatRepository.findAllById(txnInfo.getListSeatId());

        for (SeatEntity seat : seatEntities) {
            Long seatTypeId = seat.getSeatType().getId();
            if(!ticketPriceMap.containsKey(seatTypeId)) {
                TicketPriceEntity ticketPrice = ticketPriceService.getSeatTicketPrice(
                        cinemaType,
                        screenRoomType,
                        seatTypeId,
                        date,
                        time
                );
                ticketPriceMap.put(seatTypeId, ticketPrice);
            }
            TicketEntity ticketEntity = new TicketEntity();
            ticketEntity.setTicketPrice(ticketPriceMap.get(seatTypeId));
            ticketEntity.setInvoice(invoiceEntity);
            ticketEntity.setShowTime(showTime);
            ticketEntity.setSeat(seat);
            ticketRepository.save(ticketEntity);
            continue;
        }

    }
}
