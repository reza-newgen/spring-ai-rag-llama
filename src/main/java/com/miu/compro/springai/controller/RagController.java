package com.miu.compro.springai.controller;

import com.miu.compro.springai.service.rag.RagService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/ragload")
public class RagController {

    private final RagService ragService;


    public RagController(RagService ragService) {
        this.ragService = ragService;
    }

    @GetMapping("/files")
    public ResponseEntity<?> loadAllFiles() throws IOException {
        ragService.loadRag();
        return ResponseEntity.ok("Loaded Rags successfully");
    }
}
