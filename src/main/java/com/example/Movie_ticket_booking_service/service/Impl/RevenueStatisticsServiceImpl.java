package com.example.Movie_ticket_booking_service.service.Impl;

import com.example.Movie_ticket_booking_service.dto.response.CinemaRevenue;
import com.example.Movie_ticket_booking_service.dto.response.MovieRevenue;
import com.example.Movie_ticket_booking_service.dto.response.RevenueDashboardResponse;
import com.example.Movie_ticket_booking_service.dto.response.StatisticSummary;
import com.example.Movie_ticket_booking_service.entity.InvoiceEntity;
import com.example.Movie_ticket_booking_service.repository.InvoiceRepository;
import com.example.Movie_ticket_booking_service.repository.TicketRepository;
import com.example.Movie_ticket_booking_service.service.RevenueStatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RevenueStatisticsServiceImpl implements RevenueStatisticsService {
    private final InvoiceRepository  invoiceRepository;
    private final TicketRepository ticketRepository;

    @Override
    public RevenueDashboardResponse getDashboardRevenue() {

        List<InvoiceEntity> invoiceEntities = invoiceRepository.findAllByMonthAndYear(
                LocalDate.now().getMonthValue(),
                LocalDate.now().getYear()
        );

        int todayTicket = 0;
        int monthTicket = 0;
        BigDecimal todayRevenue = new BigDecimal(0);
        BigDecimal monthRevenue =  new BigDecimal(0);

        RevenueDashboardResponse response = new RevenueDashboardResponse();

        for (InvoiceEntity invoiceEntity : invoiceEntities) {
            if(invoiceEntity.getCreateDate().equals(LocalDate.now())) {
                todayTicket = todayTicket + invoiceEntity.getTickets().size();
                todayRevenue = todayRevenue.add(invoiceEntity.getTotalMoney());
            }
            monthRevenue = monthRevenue.add(invoiceEntity.getTotalMoney());
            monthTicket = monthTicket + invoiceEntity.getTickets().size();
        }

        return RevenueDashboardResponse.builder()
                .todayRevenue(todayRevenue)
                .monthRevenue(monthRevenue)
                .monthTicket(monthTicket)
                .todayTicket(todayTicket)
                .build();

    }

    @Override
    public List<StatisticSummary> getRevenueList() {
        List<InvoiceEntity> invoiceEntities = invoiceRepository.findAllByYear(LocalDate.now().getYear());
        Map<Integer, StatisticSummary> summaryMap = new HashMap<>();

        for (InvoiceEntity invoice : invoiceEntities) {
            int month = invoice.getCreateDate().getMonthValue();

            StatisticSummary summary = summaryMap.computeIfAbsent(month, m -> {
                StatisticSummary s = new StatisticSummary();
                s.setMonth(m);
                s.setMonthRevenue(BigDecimal.ZERO);
                s.setMonthTicket(0);
                return s;
            });

            int invoiceTickets = invoice.getTickets().size();
            BigDecimal invoiceRevenue = invoice.getTotalMoney();

            summary.setMonthTicket(summary.getMonthTicket() + invoiceTickets);
            summary.setMonthRevenue(summary.getMonthRevenue().add(invoiceRevenue));
        }


        return summaryMap.values().stream()
                .sorted(Comparator.comparingInt(StatisticSummary::getMonth))
                .collect(Collectors.toList());
    }

    @Override
    public List<MovieRevenue> getMovieRevenue(LocalDate startDate, LocalDate endDate) {
        List<InvoiceEntity> invoiceEntities = invoiceRepository.findByCreateDateBetween(startDate, endDate);

        Map<String, MovieRevenue> movieRevenueMap = new HashMap<>();
        for (InvoiceEntity invoice : invoiceEntities) {
            String movieName = invoice.getTickets().getFirst().getShowTime().getMovie().getName();

            MovieRevenue movie = movieRevenueMap.computeIfAbsent(movieName, mn -> {
                MovieRevenue mr = new MovieRevenue();
                mr.setMovieName(mn);
                mr.setTotalRevenue(BigDecimal.ZERO);
                mr.setTotalTicket(0);
                return mr;
            });

            int movieTickets = invoice.getTickets().size();
            BigDecimal movieRevenue = invoice.getTotalMoney();

            movie.setTotalRevenue(movie.getTotalRevenue().add(movieRevenue));
            movie.setTotalTicket(movie.getTotalTicket() + movieTickets);
        }

        List<MovieRevenue> result = new ArrayList<>(movieRevenueMap.values());
        return  result;
    }

    @Override
    public List<CinemaRevenue> getCinemaRevenue(LocalDate startDate, LocalDate endDate) {
        List<InvoiceEntity> invoiceEntities = invoiceRepository.findByCreateDateBetween(startDate, endDate);

        Map<String, CinemaRevenue> cinemaRevenueMap = new HashMap<>();
        for (InvoiceEntity invoice : invoiceEntities) {
            String cinemaName = invoice.getTickets().getFirst().getShowTime().getScreenRoom().getCinema().getName();

            CinemaRevenue cinema = cinemaRevenueMap.computeIfAbsent(cinemaName, cn -> {
                CinemaRevenue mr = new CinemaRevenue();
                mr.setCinemaName(cn);
                mr.setTotalRevenue(BigDecimal.ZERO);
                mr.setTotalTicket(0);
                return mr;
            });

            int cinemaTickets = invoice.getTickets().size();
            BigDecimal cinemaRevenue = invoice.getTotalMoney();

            cinema.setTotalRevenue(cinema.getTotalRevenue().add(cinemaRevenue));
            cinema.setTotalTicket(cinema.getTotalTicket() + cinemaTickets);
        }

        List<CinemaRevenue> result = new ArrayList<>(cinemaRevenueMap.values());
        return  result;
    }
}
