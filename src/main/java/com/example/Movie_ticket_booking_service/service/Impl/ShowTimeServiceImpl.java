package com.example.Movie_ticket_booking_service.service.Impl;

import com.example.Movie_ticket_booking_service.components.ShowTimeValidator;
import com.example.Movie_ticket_booking_service.dto.chatResponse.ShowTimeDto;
import com.example.Movie_ticket_booking_service.dto.request.FilterCinema;
import com.example.Movie_ticket_booking_service.dto.request.FilterMovie;
import com.example.Movie_ticket_booking_service.dto.request.ShowTimeRequest;
import com.example.Movie_ticket_booking_service.dto.response.*;
import com.example.Movie_ticket_booking_service.entity.CinemaEntity;
import com.example.Movie_ticket_booking_service.entity.MovieEntity;
import com.example.Movie_ticket_booking_service.entity.ScreenRoomEntity;
import com.example.Movie_ticket_booking_service.entity.ShowTimeEntity;
import com.example.Movie_ticket_booking_service.exception.AppException;
import com.example.Movie_ticket_booking_service.exception.ErrorCode;
import com.example.Movie_ticket_booking_service.repository.CinemaRepository;
import com.example.Movie_ticket_booking_service.repository.MovieRepository;
import com.example.Movie_ticket_booking_service.repository.ScreenRoomRepository;
import com.example.Movie_ticket_booking_service.repository.ShowTimeRepository;
import com.example.Movie_ticket_booking_service.service.ShowTimeService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ShowTimeServiceImpl implements ShowTimeService {
    private final ShowTimeRepository specialDayRepository;
    private final ModelMapper modelMapper;
    private final ShowTimeRepository showTimeRepository;
    private final MovieRepository movieRepository;
    private final ScreenRoomRepository screenRoomRepository;
    private final ShowTimeValidator showTimeValidator;
    private final CinemaRepository cinemaRepository;

    public MovieEntity getMoive(Long id){
        MovieEntity movie = movieRepository.findById(id)
                .orElseThrow(()->new AppException(ErrorCode.MOVIE_NOT_EXISTED));
        return movie;
    }

    public ScreenRoomEntity getScreenRoom(Long id){
        ScreenRoomEntity screenRoom = screenRoomRepository.findById(id)
                .orElseThrow(()->new AppException(ErrorCode.SCREEN_ROOM_NOT_EXISTED));
        return screenRoom;
    }

    public ShowTimeEntity getShowTimeEntity(Long id){
        ShowTimeEntity showTime = showTimeRepository.findById(id)
                .orElseThrow(()->new AppException(ErrorCode.SHOW_TIME_NOT_EXISTED));
        return showTime;
    }


    @Override
    public ShowTimeResponse createShowTime(ShowTimeRequest showTimeRequest) {
        MovieEntity movie = getMoive(showTimeRequest.getMovieId());
        ScreenRoomEntity screenRoom = getScreenRoom(showTimeRequest.getScreenRoomId());

        long minutes = Duration.between(showTimeRequest.getStartTime(), showTimeRequest.getEndTime()).toMinutes();
        if(!showTimeRequest.getEndTime().isAfter(showTimeRequest.getStartTime()) || minutes > 3 * 60){
            throw new AppException(ErrorCode.INVALID_TIME_RANGE);
        }

        showTimeValidator.checkOverlap(showTimeRequest, null);

        ShowTimeEntity showTimeEntity = modelMapper.map(showTimeRequest, ShowTimeEntity.class);
        showTimeEntity.setMovie(movie);
        showTimeEntity.setScreenRoom(screenRoom);
        showTimeEntity.setStatus(true);
        try{
            ShowTimeResponse showTimeResponse =  new ShowTimeResponse();
            showTimeResponse.setMovieId(movie.getId());
            showTimeResponse.setScreenRoomId(screenRoom.getId());
            modelMapper.map(showTimeRepository.save(showTimeEntity), showTimeResponse);
            return showTimeResponse;
        }catch (DataIntegrityViolationException ex){
            throw new AppException(ErrorCode.DATA_VIOLATION);
        }
    }

    @Override
    public List<MovieShowTimeResponse> getListShowTimeFilterCinema(FilterCinema filterCinema) {
        if(!cinemaRepository.existsById(filterCinema.getId())){
            throw new AppException(ErrorCode.CINEMA_NOT_EXISTED);
        }
        LocalTime currentTime = filterCinema.getDate().equals(LocalDate.now())
                ? LocalTime.now().minusMinutes(20)
                : LocalTime.MIDNIGHT;

        List<ShowTimeEntity> showTimes = showTimeRepository.findByScreenRoom_Cinema_IdAndShowDateAndStartTimeAfterAndStatusTrue(
                filterCinema.getId(),
                filterCinema.getDate(),
                currentTime
        );
        Map<MovieEntity , List<ShowTimeEntity>> grouped = showTimes.stream()
                .collect(Collectors.groupingBy(ShowTimeEntity::getMovie));
        List<MovieShowTimeResponse> movieShowTimeResponseList = new ArrayList<>();

        for(Map.Entry<MovieEntity, List<ShowTimeEntity>>  entry : grouped.entrySet()) {
            MovieEntity movieEntity = entry.getKey();
            List<ShowTimeEntity> showTimeEntityList =  entry.getValue();

            MovieShowTimeResponse movieDto = new MovieShowTimeResponse();
            movieDto.setMovieId(movieEntity.getId());
            movieDto.setMovieName(movieEntity.getName());
            movieDto.setPosterUrl(movieEntity.getPosterUrl());

            List<MovieShowTimeResponse.ShowTimeSummaryResponse> showTimeDtoList = showTimeEntityList.stream()
                    .map(st -> {
                        MovieShowTimeResponse.ShowTimeSummaryResponse showTimeDto =
                                new  MovieShowTimeResponse.ShowTimeSummaryResponse();
                        showTimeDto.setShowTimeId(st.getId());
                        showTimeDto.setStartTime(st.getStartTime());
                        showTimeDto.setEndTime(st.getEndTime());
                        return showTimeDto;
                    })
                    .toList();
            movieDto.setShowTimeSummaryResponseList(showTimeDtoList);
            movieShowTimeResponseList.add(movieDto);
        }
        return movieShowTimeResponseList;
    }

    @Override
    public List<CinemaShowTimeResponse> getListShowTimeFilterMovie(FilterMovie filterMovie) {
        if(!movieRepository.existsById(filterMovie.getId())) {
            throw new AppException(ErrorCode.MOVIE_NOT_EXISTED);
        }

        LocalTime currentTime = filterMovie.getDate().equals(LocalDate.now())
                ? LocalTime.now().minusMinutes(20)
                : LocalTime.MIDNIGHT;
        List<ShowTimeEntity> showTimeList = showTimeRepository.findByScreenRoom_Cinema_AddressAndMovie_IdAndShowDateAndStartTimeAfterAndStatusTrue(
                filterMovie.getAddress(),
                filterMovie.getId(),
                filterMovie.getDate(),
                currentTime
        );
        Map<CinemaEntity , List<ShowTimeEntity>> grouped = showTimeList.stream()
                .collect(Collectors.groupingBy(st -> st.getScreenRoom().getCinema()));

        List<CinemaShowTimeResponse> cinemaShowTimeResponseList =  new ArrayList<>();

        for(Map.Entry<CinemaEntity , List<ShowTimeEntity>>  entry : grouped.entrySet()) {
            CinemaEntity cinemaEntity = entry.getKey();
            List<ShowTimeEntity> showTimeEntityList =  entry.getValue();

            CinemaShowTimeResponse cinemaDto = new CinemaShowTimeResponse();
            cinemaDto.setCinemaId(cinemaEntity.getId());
            cinemaDto.setCinemaName(cinemaEntity.getName());

            List<CinemaShowTimeResponse.ShowTimeSummaryResponse> showTimeDtoList = showTimeEntityList.stream()
                    .map(st -> {
                        CinemaShowTimeResponse.ShowTimeSummaryResponse showTimeDto =
                                new  CinemaShowTimeResponse.ShowTimeSummaryResponse();
                        showTimeDto.setShowTimeId(st.getId());
                        showTimeDto.setStartTime(st.getStartTime());
                        showTimeDto.setEndTime(st.getEndTime());
                        return showTimeDto;
                    })
                    .toList();
            cinemaDto.setShowTimeSummaryResponseList(showTimeDtoList);
            cinemaShowTimeResponseList.add(cinemaDto);
        }
        return cinemaShowTimeResponseList;
    }

    @Override
    public List<ShowTimeDto> getShowTimeDto(String movieName, String cinemaName, LocalDate date) {
        List<ShowTimeEntity> showTimeEntityList = showTimeRepository.findByMovie_NameAndScreenRoom_Cinema_NameAndShowDateAndStatusTrue(
                movieName, cinemaName, date
        );

        return showTimeEntityList.stream()
                .map(st -> new ShowTimeDto(
                        st.getId(),
                        st.getMovie().getName(),
                        st.getScreenRoom().getCinema().getName(),
                        st.getScreenRoom().getName(),
                        st.getShowDate().toString(),
                        st.getStartTime().toString(),
                        st.getEndTime().toString()
                )).collect(Collectors.toList());
    }

    @Override
    public void deleteShowTime(Long id) {
        ShowTimeEntity showTime = getShowTimeEntity(id);
        showTime.setStatus(false);
        showTimeRepository.save(showTime);
    }

    @Override
    public ShowTimeResponse updateShowTime(ShowTimeRequest showTimeRequest, Long id) {
        MovieEntity movie = getMoive(showTimeRequest.getMovieId());
        ScreenRoomEntity screenRoom = getScreenRoom(showTimeRequest.getScreenRoomId());
        showTimeValidator.checkOverlap(showTimeRequest, id);

        ShowTimeEntity showTimeEntity = getShowTimeEntity(id);

        modelMapper.map(showTimeRequest, showTimeEntity);
        showTimeEntity.setMovie(movie);
        showTimeEntity.setScreenRoom(screenRoom);

        try{
            ShowTimeResponse showTimeResponse =  new ShowTimeResponse();
            showTimeResponse.setMovieId(movie.getId());
            showTimeResponse.setScreenRoomId(screenRoom.getId());
            modelMapper.map(showTimeRepository.save(showTimeEntity), showTimeResponse);
            return showTimeResponse;
        }catch (DataIntegrityViolationException ex){
            throw new AppException(ErrorCode.DATA_VIOLATION);
        }
    }

    @Override
    public ShowTimeResponse getShowTime(Long id) {
        ShowTimeEntity showTime = getShowTimeEntity(id);
        ShowTimeResponse showTimeResponse = modelMapper.map(showTime, ShowTimeResponse.class);
        showTimeResponse.setMovieId(showTime.getMovie().getId());
        showTimeResponse.setScreenRoomId(showTime.getScreenRoom().getId());
        return showTimeResponse;
    }

    @Override
    public List<ShowTimeDTO> getShowTimeList(Long screenRoomId, LocalDate date) {
        List<ShowTimeEntity> showTimeEntityList = showTimeRepository.findByScreenRoom_IdAndShowDate(screenRoomId, date);
        List<ShowTimeDTO> showTimeDTOList = showTimeEntityList.stream()
                .map(st-> {
                    ShowTimeDTO showTimeDTO = ShowTimeDTO.builder()
                            .id(st.getId())
                            .movieName(st.getMovie().getName())
                            .showDate(st.getShowDate())
                            .startTime(st.getStartTime())
                            .endTime(st.getEndTime())
                            .screenRoomId(st.getScreenRoom().getId())
                            .build();
                    return showTimeDTO;
                })
                .collect(Collectors.toList());
        return showTimeDTOList;
    }

}
