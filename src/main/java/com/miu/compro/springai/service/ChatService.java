package com.miu.compro.springai.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ChatService {
    Logger LOGGER = LoggerFactory.getLogger(ChatService.class);

    private ChatClient chatClient;
    private final ChatModel chatModel;
    private final VectorStore vectorStore;

    public ChatService(ChatClient.Builder  builder, ChatModel chatModel, VectorStore vectorStore) {
        this.chatClient = builder.defaultAdvisors(new MessageChatMemoryAdvisor(new InMemoryChatMemory())).build();
        this.chatModel = chatModel;
        this.vectorStore = vectorStore;
    }


    private String prompt = """
            Your task is to answer the questions about USA Labour Market. Use the information from the DOCUMENTS
            section to provide accurate answers. If unsure or if the answer isn't found in the DOCUMENTS section, 
            simply state that you don't know the answer.
                        
            QUESTION:
            {input}
                        
            DOCUMENTS:
            {documents}
                        
            """;


    public String llamaChat(String message) {
        return chatClient.prompt()
                .user(message)
                .call()
                .content();
    }

public String ragDocumentChat(String question) {
    PromptTemplate template
            = new PromptTemplate(prompt);
    Map<String, Object> promptsParameters = new HashMap<>();
    promptsParameters.put("input", question);
    LOGGER.info("Answer is searching for {}", question);
    promptsParameters.put("documents", findSimilarData(question));

    return chatModel
            .call(template.create(promptsParameters))
            .getResult()
            .getOutput()
            .getText();
}

    private String findSimilarData(String question) {

        SearchRequest searchRequest = SearchRequest.builder()
                .query(question) // Correct way to set the query
                .topK(5)         // Setting the number of similar results to fetch
                .build();


        List<Document> documents = vectorStore.similaritySearch(searchRequest);

        return documents
                .stream()
                .map(document -> document.getFormattedContent().toString())
                .collect(Collectors.joining());

    }
}
