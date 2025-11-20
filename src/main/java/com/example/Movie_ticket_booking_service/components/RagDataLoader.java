package com.example.Movie_ticket_booking_service.components;

import com.example.Movie_ticket_booking_service.service.chatbotServices.RagIngestionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class RagDataLoader implements ApplicationRunner {
    private final RagIngestionService ragIngestionService;
    private final VectorStore vectorStore;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("Bắt đầu nạp dữ liệu RAG vào Vector Store bằng ApplicationRunner...");
        try {
            String filter = "type == 'movie' || type == 'cinema' || type == 'screen' || type == 'screen_type'";
            vectorStore.delete(filter);
            int count = ragIngestionService.ingestAll();
            log.info(" Hoan thanh nap du lieu RAG. Tong cong da nap {} du lieu.", count);
        } catch (Exception e) {
            log.error("❌ Lỗi nghiêm trọng khi nạp dữ liệu RAG vào Vector Store: {}", e.getMessage(), e);
        }
    }
}
