package com.example.Movie_ticket_booking_service.controller.chatBotController;

import com.example.Movie_ticket_booking_service.dto.chatResponse.ChatRequest;
import com.example.Movie_ticket_booking_service.dto.chatResponse.ChatResponse;
import com.example.Movie_ticket_booking_service.service.chatbotServices.ChatBotService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/chatbot")
@RequiredArgsConstructor
public class ChatBotController {
    private final ChatBotService chatBotService;

    @PostMapping("/chat")
    ResponseEntity<ChatResponse> chai(@RequestBody ChatRequest request, Principal principal){
        if (request.message() == null || request.message().trim().isEmpty()) {
            ChatResponse errorRes = new ChatResponse(
                    "error",
                    "Tin nhắn không được để trống.",
                    null, null, null,
                    null, null, null, null,null
            );
            return ResponseEntity.badRequest().body(errorRes);
        }

        String authenticatedUserId = principal.getName();

        String chatId = "user:" + authenticatedUserId;

        ChatResponse response = chatBotService.getChatResponse(request.message(), chatId);

        return ResponseEntity.ok(response);
    }
}
