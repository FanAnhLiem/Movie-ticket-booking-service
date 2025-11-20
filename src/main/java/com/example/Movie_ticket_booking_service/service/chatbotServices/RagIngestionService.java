package com.example.Movie_ticket_booking_service.service.chatbotServices;

import com.example.Movie_ticket_booking_service.components.RagDocumentMapper;
import com.example.Movie_ticket_booking_service.repository.CinemaRepository;
import com.example.Movie_ticket_booking_service.repository.MovieRepository;
import com.example.Movie_ticket_booking_service.repository.ScreenRoomRepository;
import com.example.Movie_ticket_booking_service.repository.ScreenRoomTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RagIngestionService {
    private final MovieRepository movieRepository;
    private final CinemaRepository cinemaRepository;
    private final ScreenRoomRepository screenRoomRepository;
    private final ScreenRoomTypeRepository screenRoomTypeRepository;
    private final RagDocumentMapper mapper;
    private final VectorStore vectorStore;

    private final TokenTextSplitter splitter = new TokenTextSplitter();

    @Transactional(readOnly = true)
    public int ingestAll() {
        int count = 0;
        count += ingestMovies();
        count += ingestCinemas();
        count += ingestScreens();
        count += ingestScreenTypes();
        return count;
    }

    public int ingestMovies() {
        List<Document> docs = movieRepository.findTop100ByOrderByEndDateDesc()
                .stream()
                .map(mapper::fromMovie)
                .flatMap(d -> splitter.split(d).stream())
                .toList();
        if (!docs.isEmpty()) vectorStore.add(docs);
        return docs.size();
    }

    public int ingestCinemas() {
        var docs = cinemaRepository.findAll()
                .stream()
                .map(mapper::fromCinema)
                .flatMap(d -> splitter.split(d).stream())
                .toList();
        if (!docs.isEmpty()) vectorStore.add(docs);
        return docs.size();
    }

    public int ingestScreens() {
        var docs = screenRoomRepository.findAll()
                .stream()
                .map(mapper::fromScreenRoom)
                .flatMap(d -> splitter.split(d).stream())
                .toList();
        if (!docs.isEmpty()) vectorStore.add(docs);
        return docs.size();
    }

    public int ingestScreenTypes() {
        var docs = screenRoomTypeRepository.findAll()
                .stream()
                .map(mapper::fromScreenRoomType)
                .flatMap(d -> splitter.split(d).stream())
                .toList();
        if (!docs.isEmpty()) vectorStore.add(docs);
        return docs.size();
    }

}
