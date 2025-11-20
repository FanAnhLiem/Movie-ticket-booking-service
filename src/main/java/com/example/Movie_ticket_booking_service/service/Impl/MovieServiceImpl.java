package com.example.Movie_ticket_booking_service.service.Impl;

import com.example.Movie_ticket_booking_service.dto.request.FilterMovie;
import com.example.Movie_ticket_booking_service.dto.response.MoviePage;
import com.example.Movie_ticket_booking_service.dto.response.MovieResponse;
import com.example.Movie_ticket_booking_service.dto.response.MovieSummaryResponse;
import com.example.Movie_ticket_booking_service.entity.*;
import com.example.Movie_ticket_booking_service.exception.AppException;
import com.example.Movie_ticket_booking_service.exception.ErrorCode;
import com.example.Movie_ticket_booking_service.dto.request.MovieRequest;
import com.example.Movie_ticket_booking_service.dto.response.MovieDetailResponse;
import com.example.Movie_ticket_booking_service.repository.*;
import com.example.Movie_ticket_booking_service.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static io.lettuce.core.GeoArgs.Unit.m;

@Service
@RequiredArgsConstructor
public class MovieServiceImpl implements MovieService {
    private final MovieRepository movieRepository;
    private final ModelMapper modelMapper;
    private final CinemaRepository cinemaRepository;
    private final ScreenRoomRepository screenRoomRepository;
    private final ShowTimeRepository showTimeRepository;
    private final FirebaseStorageService storageService;


    private static final long MAX_FILE_SIZE = 10L * 1024 * 1024;

    private void validatePoster(MultipartFile file) {
        if (file.getSize() > MAX_FILE_SIZE) {
            throw new AppException(ErrorCode.FILE_TOO_LARGE);
        }

        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new AppException(ErrorCode.UNSUPPORTED_FILE_TYPE);
        }

        String filename = file.getOriginalFilename();
        if (filename != null) {
            String lower = filename.toLowerCase();
            if (!(lower.endsWith(".jpg") || lower.endsWith(".jpeg") || lower.endsWith(".png") || lower.endsWith(".webp"))) {
                throw new AppException(ErrorCode.UNSUPPORTED_FILE_TYPE);
            }
        }
    }

    @Override
    public MovieDetailResponse createMovie(MovieRequest movieRequest, MultipartFile posterFile) {
        if (movieRepository.findByName(movieRequest.getName()).isPresent()) {
            throw new AppException(ErrorCode.MOVIE_EXISTED);
        }

        if(movieRequest.getReleaseDate().isAfter(movieRequest.getEndDate())) {
            throw new AppException(ErrorCode.INVALID_DATE_RANGE);
        }

        if (posterFile == null || posterFile.isEmpty()) {
            throw new AppException(ErrorCode.FILE_REQUIRED);
        }
        validatePoster(posterFile);

        String posterUrl;
        try {
            posterUrl = storageService.uploadFile(posterFile);
        } catch (IOException e) {
            throw new AppException(ErrorCode.UPLOAD_FAILED);
        }

        MovieEntity movieEntity = modelMapper.map(movieRequest, MovieEntity.class);
        movieEntity.setPosterUrl(posterUrl);
        movieEntity.setStatus(true);
        movieEntity.setCreatedAt(LocalDateTime.now());
        try {
            MovieEntity saved = movieRepository.save(movieEntity);
            return modelMapper.map(saved, MovieDetailResponse.class);

        } catch (DataIntegrityViolationException e) {
            throw new AppException(ErrorCode.DATA_VIOLATION);
        }
    }


    @Override
    public MovieDetailResponse updateMovie(Long id, MovieRequest movieRequest, MultipartFile posterFile) {
        MovieEntity movieEntity = movieRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.MOVIE_NOT_EXISTED));
        if(movieRequest.getReleaseDate().isAfter(movieRequest.getEndDate())) {
            throw new AppException(ErrorCode.INVALID_DATE_RANGE);
        }
        modelMapper.map(movieRequest, movieEntity);
        if(posterFile != null || !posterFile.isEmpty()){
            validatePoster(posterFile);

            String posterUrl;
            try {
                posterUrl = storageService.uploadFile(posterFile);
            } catch (IOException e) {
                throw new AppException(ErrorCode.UPLOAD_FAILED);
            }
            movieEntity.setPosterUrl(posterUrl);

            movieEntity.setUpdatedAt(LocalDateTime.now());
        }
        try {
            MovieEntity saved = movieRepository.save(movieEntity);
            return modelMapper.map(saved, MovieDetailResponse.class);

        } catch (DataIntegrityViolationException e) {
            throw new AppException(ErrorCode.DATA_VIOLATION);
        }
    }

    @Override
    public void updateIsActiveMovie(Long id) {
        var movieEntity = movieRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.MOVIE_NOT_EXISTED));
        movieEntity.setStatus(true);
        movieRepository.save(movieEntity);
    }

    @Override
    public void deleteMovie(Long id) {
        var movieEntity = movieRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.MOVIE_NOT_EXISTED));
        movieEntity.setStatus(false);
        movieRepository.save(movieEntity);
    }

    @Override
    public MovieDetailResponse getMovie(Long id) {
        var movieEntity = movieRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.MOVIE_NOT_EXISTED));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        MovieDetailResponse movieDetailResponse = modelMapper.map(movieEntity, MovieDetailResponse.class);
        movieDetailResponse.setCreateAt(movieEntity.getCreatedAt().format(formatter));
        movieDetailResponse.setReleaseDate(movieEntity.getReleaseDate().format(formatter));
        movieDetailResponse.setEndDate(movieEntity.getEndDate().format(formatter));
        return movieDetailResponse;
    }

    @Override
    public List<MovieSummaryResponse> getAllMovies() {
        try {
            List<MovieEntity> movieEntities = movieRepository.findAll();
            List<MovieSummaryResponse> movieResponseList = new ArrayList<>();
            for(MovieEntity movieEntity : movieEntities){
                MovieSummaryResponse movieSummaryResponse = modelMapper.map(movieEntity, MovieSummaryResponse.class);
                movieResponseList.add(movieSummaryResponse);
            }
            return  movieResponseList;
        }catch (RuntimeException e){
            throw new AppException(ErrorCode.MOVIE_NOT_EXISTED);
        }
    }

    @Override
    public List<MovieSummaryResponse> getNowPlayingMovies() {
        List<MovieEntity> movieEntities = movieRepository.findNowPlayingMovies(LocalDate.now());

        List<MovieSummaryResponse> movieResponseList = movieEntities.stream()
                .map(movieEntity -> {
                    MovieSummaryResponse movieDetailResponse = modelMapper.map(movieEntity, MovieSummaryResponse.class);
                    return movieDetailResponse;
                })
                .toList();
        return movieResponseList;
    }

    @Override
    public List<MovieSummaryResponse> getUpcomingMovies() {
        List<MovieEntity> movieEntities = movieRepository.findUpcomingMovies(LocalDate.now());

        List<MovieSummaryResponse> movieResponseList = movieEntities.stream()
                .map(movieEntity -> {
                    MovieSummaryResponse movieDetailResponse = modelMapper.map(movieEntity, MovieSummaryResponse.class);
                    return movieDetailResponse;
                })
                .toList();
        return movieResponseList;
    }


    @Override
    public List<String> getListCinemaAddress(FilterMovie filterMovie) {
        if(!movieRepository.existsById(filterMovie.getId())) {
            throw new AppException(ErrorCode.MOVIE_NOT_EXISTED);
        }
        LocalTime currentTime = filterMovie.getDate().equals(LocalDate.now())
                ? LocalTime.now().minusMinutes(20)
                : LocalTime.MIDNIGHT;

        List<ShowTimeEntity> showTimeEntityList = showTimeRepository.findByMovie_IdAndShowDateAndStartTimeAfterAndStatusTrue(
                filterMovie.getId(),
                filterMovie.getDate(),
                currentTime
        );
        Set<String> cinemaAddressList = new HashSet<>();

        for(ShowTimeEntity st : showTimeEntityList){
            String address = st.getScreenRoom().getCinema().getAddress();
            cinemaAddressList.add(address);
        }
        List<String> addressList = new ArrayList<>(cinemaAddressList);
        return addressList;
    }

    public String getStatus(MovieEntity movieEntity) {
        if(movieEntity.getReleaseDate().isAfter(LocalDate.now())){
            return "Sắp chiếu";
        }
        if(movieEntity.getEndDate().isBefore(LocalDate.now())){
            return "Ngừng chiếu";
        }
        return "Đang chiếu";
    }

    @Override
    public Page<MoviePage> getMoviePage(PageRequest pageRequest) {
        Page<MovieEntity> movieEntities = movieRepository.findAll(pageRequest);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        return movieEntities.map(m -> {
                    return MoviePage.builder()
                            .id(m.getId())
                            .name(m.getName())
                            .yearRelease(m.getReleaseDate().getYear())
                            .posterUrl(m.getPosterUrl())
                            .category(m.getCategory())
                            .showSchedule(m.getReleaseDate().format(formatter) + " - " + m.getEndDate().format(formatter))
                            .status(getStatus(m))
                            .creatAt(m.getCreatedAt().format(formatter))
                            .build();
                });
    }

    @Override
    public List<MovieResponse> getMovieShowDay(LocalDate date) {
        List<MovieEntity> movieEntityList = movieRepository.findNowPlayingMovies(date);
        return  movieEntityList.stream()
                .map(m -> modelMapper.map(m, MovieResponse.class))
                .collect(Collectors.toList());
    }


}
