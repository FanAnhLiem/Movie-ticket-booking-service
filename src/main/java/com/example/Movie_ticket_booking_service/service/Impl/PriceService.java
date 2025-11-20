package com.example.Movie_ticket_booking_service.service.Impl;

import com.example.Movie_ticket_booking_service.enums.TimeFrame;
import com.example.Movie_ticket_booking_service.repository.SpecialDayRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;

@Service
@RequiredArgsConstructor
public class PriceService {
    private final SpecialDayRepository specialDayRepository;
    private static final double base_price = 100000.0;


    public Double finalPrice(Double priceFactorCinemaType,
                             Double priceFactorScreenRoomType,
                             Double priceFactorSeatType,
                             Boolean specialDay,
                             TimeFrame timeFrame
                             )
    {
        double priceBase = base_price;
        //giá theo rạp
        priceBase *= priceFactorCinemaType;

        //giá theo loại ghế
        priceBase *= priceFactorScreenRoomType;

        priceBase *= priceFactorSeatType;

        //giá theo khung giờ
        priceBase *= timeFrame.getPriceFactor();

        if(specialDay){
            priceBase *= 1.1;
        }

        double finalPrice = priceBase;
        return finalPrice;
    }

    public boolean isWeekend(LocalDate showDate) {
        DayOfWeek d = showDate.getDayOfWeek();
        return d == DayOfWeek.SATURDAY || d == DayOfWeek.SUNDAY;
    }

    public boolean isSpecialDay(LocalDate showDate) {
        int m = showDate.getMonthValue();
        int d = showDate.getDayOfMonth();
        return specialDayRepository.existsByMonthAndDay(m, d);
    }

}
