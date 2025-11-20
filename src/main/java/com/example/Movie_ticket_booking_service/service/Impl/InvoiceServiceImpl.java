package com.example.Movie_ticket_booking_service.service.Impl;

import com.example.Movie_ticket_booking_service.configuration.RandomCode;
import com.example.Movie_ticket_booking_service.configuration.VNPayConfig;
import com.example.Movie_ticket_booking_service.dto.TxnInfo;
import com.example.Movie_ticket_booking_service.dto.request.InvoiceRequest;
import com.example.Movie_ticket_booking_service.dto.response.InvoiceDetailAD;
import com.example.Movie_ticket_booking_service.dto.response.InvoiceDetailResponse;
import com.example.Movie_ticket_booking_service.dto.response.InvoiceResponse;
import com.example.Movie_ticket_booking_service.dto.response.InvoiceSummary;
import com.example.Movie_ticket_booking_service.entity.*;
import com.example.Movie_ticket_booking_service.enums.InvoiceStatus;
import com.example.Movie_ticket_booking_service.enums.TimeFrame;
import com.example.Movie_ticket_booking_service.exception.AppException;
import com.example.Movie_ticket_booking_service.exception.ErrorCode;
import com.example.Movie_ticket_booking_service.repository.*;
import com.example.Movie_ticket_booking_service.service.InvoiceService;
import com.example.Movie_ticket_booking_service.service.TicketPriceService;
import com.example.Movie_ticket_booking_service.service.TicketService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InvoiceServiceImpl implements InvoiceService {
    private final RedisService redisService;
    private final InvoiceRepository  invoiceRepository;
    private final UserRepository userRepository;
    private final TicketService  ticketService;
    private final TicketRepository ticketRepository;
    private final TicketPriceService ticketPriceService;
    private final SeatRepository seatRepository;
    private final ShowTimeRepository showTimeRepository;
    private final PriceService priceService;

    public UserEntity getUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails user = (UserDetails) authentication.getPrincipal();
        UserEntity userEntity = userRepository.findByEmail(user.getUsername())
                .orElseThrow(()-> new AppException(ErrorCode.USER_NOT_EXISTED));
        return userEntity;
    }

    private BigDecimal parseAmount(String vnpAmount) {
        long amount = Long.parseLong(vnpAmount);
        return BigDecimal.valueOf(amount);
    }

    @Transactional
    @Override
    public void createInvoice(InvoiceRequest invoiceRequest) {
        ShowTimeEntity showTime = showTimeRepository.findById(invoiceRequest.getShowtimeId())
                .orElseThrow(() -> new AppException(ErrorCode.SHOW_TIME_NOT_EXISTED));

        Long cinemaType = showTime.getScreenRoom().getCinema().getCinemaType().getId();
        Long screenRoomType = showTime.getScreenRoom().getScreenRoomType().getId();
        LocalDate date = showTime.getShowDate();
        LocalTime time = showTime.getStartTime();

        Map<Long, TicketPriceEntity> ticketPriceMap = new HashMap<>();

        List<SeatEntity> seatEntities = seatRepository.findAllById(invoiceRequest.getListSeatId());

        TxnInfo txnInfo = new TxnInfo();
        txnInfo.setListSeatId(invoiceRequest.getListSeatId());
        txnInfo.setShowTimeId(invoiceRequest.getShowtimeId());
        redisService.txnRefInfo(invoiceRequest.getVnp_TxnRef(), txnInfo, 10L);
        BigDecimal totalMoney = new BigDecimal(0);

        for (SeatEntity seat : seatEntities) {
            redisService.seatHolding(invoiceRequest.getShowtimeId(), seat.getId(), 10L);
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
            totalMoney = totalMoney.add(ticketPriceMap.get(seatTypeId).getPrice());
            continue;
        }

        if(!totalMoney.equals(parseAmount(invoiceRequest.getVnp_Amount()))){
            throw new AppException(ErrorCode.INVALID_PRICE);
        }

        InvoiceEntity invoiceEntity = new InvoiceEntity();

        invoiceEntity.setCreateDate(LocalDate.now());
        invoiceEntity.setCreateTime(LocalTime.now());
        invoiceEntity.setUser(getUser());
        invoiceEntity.setTotalMoney(totalMoney);
        invoiceEntity.setTxnRef(invoiceRequest.getVnp_TxnRef());
        invoiceEntity.setStatus(InvoiceStatus.PENDING);

        try {
            invoiceRepository.save(invoiceEntity);
        } catch (DataAccessException dae) {
            throw new AppException(ErrorCode.ORDER_CREATE_FAILED);
        }
    }

    @Override
    public void updateInvoice(String vnp_TxnRef) {
        InvoiceEntity invoiceEntity = invoiceRepository.findByTxnRef(vnp_TxnRef)
                .orElseThrow(()->new AppException(ErrorCode.INVOICE_NOT_EXISTED));
        String bookingCode = RandomCode.generate(12);
        invoiceEntity.setStatus(InvoiceStatus.PAID);
        invoiceEntity.setBookingCode(bookingCode);
        try {
            ticketService.createTicket(vnp_TxnRef, invoiceRepository.save(invoiceEntity));
        }catch (DataAccessException e){
            throw new AppException(ErrorCode.DATA_VIOLATION);
        }
    }

    @Override
    public List<InvoiceResponse> getInvoiceList() {
        UserEntity user = getUser();
        List<InvoiceEntity> invoiceEntityList = invoiceRepository.findByUser_Id(user.getId());
        List<InvoiceResponse> invoiceResponseList = new ArrayList<>();
        for (InvoiceEntity invoiceEntity : invoiceEntityList) {
            TicketEntity ticketEntity = getTicket(invoiceEntity.getId());
            int totalTicket = ticketRepository.countByInvoice_Id(invoiceEntity.getId());

            InvoiceResponse invoiceResponse = new InvoiceResponse();
            invoiceResponse.setInvoiceId(invoiceEntity.getId());
            invoiceResponse.setTotalMoney(invoiceEntity.getTotalMoney());
            invoiceResponse.setMovieName(ticketEntity.getShowTime().getMovie().getName());
            invoiceResponse.setTotalTicket(totalTicket);
            invoiceResponse.setShowDate(ticketEntity.getShowTime().getShowDate());
            invoiceResponse.setStartTime(ticketEntity.getShowTime().getStartTime());

            invoiceResponseList.add(invoiceResponse);
        }
        return invoiceResponseList;
    }

    TicketEntity getTicket(Long invoiceId) {
        TicketEntity ticketEntity = ticketRepository.findFirstByInvoice_Id(invoiceId)
                .orElseThrow(()->new AppException(ErrorCode.INVOICE_NOT_EXISTED));
        return ticketEntity;
    }

    @Override
    public InvoiceDetailResponse getInvoice(Long invoiceId) {
        InvoiceEntity invoiceEntity = invoiceRepository.findById(invoiceId)
                .orElseThrow(()->new AppException(ErrorCode.INVOICE_NOT_EXISTED));
        InvoiceDetailResponse invoiceDetailResponse = getInvoiceDetail(invoiceEntity);
        return invoiceDetailResponse;
    }

    @Override
    public InvoiceDetailResponse getInvoiceByBookingCode(String bookingCode) {
        InvoiceEntity invoiceEntity = invoiceRepository.findByBookingCode(bookingCode)
                .orElseThrow(()->new AppException(ErrorCode.INVOICE_NOT_EXISTED));
        InvoiceDetailResponse invoiceDetailResponse = getInvoiceDetail(invoiceEntity);
        return invoiceDetailResponse;
    }

    @Override
    public void checkInInvoice(Long invoiceId) {
        InvoiceEntity invoiceEntity = invoiceRepository.findById(invoiceId)
                .orElseThrow(()->new AppException(ErrorCode.INVOICE_NOT_EXISTED));
        invoiceEntity.setStatus(InvoiceStatus.CHECKED_IN);
        invoiceRepository.save(invoiceEntity);
    }

    @Override
    public List<InvoiceSummary> getInvoiceSummaryList() {
        List<InvoiceEntity> invoiceEntityList = invoiceRepository.findAll();
        List<InvoiceSummary> invoiceSummaryList = new ArrayList<>();
        for (InvoiceEntity invoiceEntity : invoiceEntityList) {
            TicketEntity ticketEntity = getTicket(invoiceEntity.getId());
            String showtime = ticketEntity.getShowTime().getStartTime().toString() + "-" +
                    ticketEntity.getShowTime().getEndTime().toString() + " " +
                    ticketEntity.getShowTime().getShowDate().toString();
            String screenRoom = ticketEntity.getShowTime().getScreenRoom().getName() + " - " +
                    ticketEntity.getShowTime().getScreenRoom().getCinema().getName();
            InvoiceSummary invoiceSummary = new InvoiceSummary();
            invoiceSummary.setId(invoiceEntity.getId());
            invoiceSummary.setCode(invoiceEntity.getBookingCode());
            invoiceSummary.setMovieName(ticketEntity.getShowTime().getMovie().getName());
            invoiceSummary.setShowTime(showtime);
            invoiceSummary.setScreenRoom(screenRoom);
            invoiceSummary.setTotalMoney(invoiceEntity.getTotalMoney());
            invoiceSummary.setCreateDate(invoiceEntity.getCreateDate().toString());
            invoiceSummaryList.add(invoiceSummary);
        }
        return invoiceSummaryList;
    }

    @Override
    public InvoiceDetailAD getInvoiceDetail(Long id) {
        InvoiceEntity invoiceEntity = invoiceRepository.findById(id)
                .orElseThrow(()->new AppException(ErrorCode.INVOICE_NOT_EXISTED));
        TicketEntity ticketEntity = getTicket(invoiceEntity.getId());
        List<InvoiceDetailAD.SeatInvoice> seatInvoices = invoiceEntity.getTickets().stream()
                .map(t -> {InvoiceDetailAD.SeatInvoice seat = new InvoiceDetailAD.SeatInvoice();
                        seat.setId(t.getId());
                        seat.setSeatCode(t.getSeat().getSeatCode());
                        seat.setPrice(t.getTicketPrice().getPrice());
                        return seat;
                }).toList();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return InvoiceDetailAD.builder()
                .invoiceId(invoiceEntity.getId())
                .bookingCode(invoiceEntity.getBookingCode())
                .movieName(ticketEntity.getShowTime().getMovie().getName())
                .screenRoomName(ticketEntity.getShowTime().getScreenRoom().getCinema().getName())
                .screenRoomTypeName(ticketEntity.getShowTime().getScreenRoom().getCinema().getCinemaType().getName())
                .cinema(ticketEntity.getShowTime().getScreenRoom().getCinema().getName())
                .showTime(ticketEntity.getShowTime().getStartTime().toString() + "-" +
                        ticketEntity.getShowTime().getEndTime().toString())
                .showDate(ticketEntity.getShowTime().getShowDate().format(formatter))
                .bookDay(invoiceEntity.getCreateDate().format(formatter))
                .userId(invoiceEntity.getUser().getId())
                .userName(invoiceEntity.getUser().getName())
                .userEmail(invoiceEntity.getUser().getEmail())
                .userPhone(invoiceEntity.getUser().getPhone())
                .status(invoiceEntity.getStatus())
                .totalMoney(invoiceEntity.getTotalMoney())
                .seatList(seatInvoices)
                .build();
    }


    public InvoiceDetailResponse getInvoiceDetail(InvoiceEntity invoiceEntity) {
        TicketEntity ticketEntity = getTicket(invoiceEntity.getId());

        List<String> seatCodeList = ticketRepository.findSeatCodesByInvoiceId(invoiceEntity.getId());

        InvoiceDetailResponse invoiceDetailResponse = new InvoiceDetailResponse();

        invoiceDetailResponse.setBookingCode(invoiceEntity.getBookingCode());
        invoiceDetailResponse.setMovieName(ticketEntity.getShowTime().getMovie().getName());
        invoiceDetailResponse.setTotalMoney(invoiceEntity.getTotalMoney());
        invoiceDetailResponse.setTotalTicket(seatCodeList.size());
        invoiceDetailResponse.setShowDate(ticketEntity.getShowTime().getShowDate());
        invoiceDetailResponse.setStartTime(ticketEntity.getShowTime().getStartTime());
        invoiceDetailResponse.setScreenRoomName(ticketEntity.getShowTime().getScreenRoom().getName());
        invoiceDetailResponse.setScreenRoomType(ticketEntity.getShowTime().getScreenRoom().getScreenRoomType().getName());
        invoiceDetailResponse.setSeatList(seatCodeList);
        invoiceDetailResponse.setUserId(invoiceEntity.getUser().getId());
        invoiceDetailResponse.setUserName(invoiceEntity.getUser().getName());
        invoiceDetailResponse.setInvoiceId(invoiceEntity.getId());
        return invoiceDetailResponse;
    }
}
