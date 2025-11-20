package com.example.Movie_ticket_booking_service.controller.Admin;

import com.example.Movie_ticket_booking_service.dto.request.MovieRequest;
import com.example.Movie_ticket_booking_service.dto.request.testRq;
import com.example.Movie_ticket_booking_service.dto.response.*;
import com.example.Movie_ticket_booking_service.service.MovieService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin/movie")
@RequiredArgsConstructor
public class MovieAdminController {
    private final MovieService movieService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<MovieDetailResponse> createMovie(@RequestParam("poster") MultipartFile poster,
                                                        @Valid @ModelAttribute MovieRequest movieRequest) {
        return ApiResponse.<MovieDetailResponse>builder()
                .result(movieService.createMovie(movieRequest, poster))
                .build();
    }

    @PutMapping("/active/{id}")
    public ApiResponse<Void> updateIsActiveMovie(@Valid @PathVariable Long id) {
        movieService.updateIsActiveMovie(id);
        return ApiResponse.<Void>builder()
                .message("Movie active success")
                .build();
    }


    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<MovieDetailResponse> updateMovie(@Valid @PathVariable Long id,
                                                        @ModelAttribute MovieRequest movieRequest,
                                                        @RequestPart("poster") MultipartFile poster) {
        return ApiResponse.<MovieDetailResponse>builder()
                .result(movieService.updateMovie(id, movieRequest, poster))
                .build();
    }

    @GetMapping
    ApiResponse<MovieListPage> getMovie(@RequestParam("page")     int page,
                                        @RequestParam("limit")    int limit ){
        PageRequest pageRequest = PageRequest.of(
                page - 1, limit,
                Sort.by("id").descending());
        Page<MoviePage> moviePage = movieService.getMoviePage(pageRequest);
        int totalPages = moviePage.getTotalPages();
        List<MoviePage> movieList = moviePage.getContent();

        return ApiResponse.<MovieListPage>builder()
                .result(MovieListPage.builder()
                        .movieList(movieList)
                        .totalPages(totalPages)
                        .build())
                .build();
    }

    @GetMapping("/showday")
    ApiResponse<List<MovieResponse>> getMovieShowDay(@RequestParam("date")
                                                     @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate date){
        return ApiResponse.<List<MovieResponse>>builder()
                .result(movieService.getMovieShowDay(date))
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<String> deleteMovie(@Valid @PathVariable Long id) {
        movieService.deleteMovie(id);
        return ApiResponse.<String>builder()
                .message("Movie delete successfully")
                .build();
    }
}
