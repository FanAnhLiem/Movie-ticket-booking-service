package com.example.Movie_ticket_booking_service.service.chatbotServices;

import com.example.Movie_ticket_booking_service.dto.chatResponse.ChatResponse;
import com.example.Movie_ticket_booking_service.service.tools.RagFilterBuilder;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatBotService {
    private final ChatClient chatClient;

    String systemPrompt = ChatSystemPrompt.cinemaJsonPrompt();

    public ChatResponse getChatResponse(String userMessage, String chatId) {
        final String filter = RagFilterBuilder.buildFilterExpression(userMessage);
        return chatClient.prompt()
                .system(systemPrompt)
                .user(userMessage)
                .advisors(a -> {
                    a.param(ChatMemory.CONVERSATION_ID, chatId);
                    a.param(QuestionAnswerAdvisor.FILTER_EXPRESSION, filter);
                })
                .call()
                .entity(ChatResponse.class);
    }

}
