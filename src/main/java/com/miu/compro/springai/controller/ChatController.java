package com.miu.compro.springai.controller;

import com.miu.compro.springai.service.ChatService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChatController {
    Logger LOGGER = LoggerFactory.getLogger(ChatController.class);
    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @GetMapping("/")
    public String chat(@RequestParam String message) {
        return chatService.llamaChat(message);
    }


    @GetMapping("/chat-labour-market")
    public String questionandAnswer(@RequestParam(value = "question",
            defaultValue = "List all the Articles in the USA Labour Market")
                                    String question) {
        LOGGER.info("question is: {}",question);
        return chatService.ragDocumentChat(question);
    }


}
