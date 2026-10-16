package com.ankursalunke.claimflow.ingestion;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/ingestions")
@RequiredArgsConstructor
public class IngestionController {

    private final ClaimIngestionService claimIngestionService;

    @PostMapping("/synthea")
    public IngestionReport ingestSynthea() {
        return claimIngestionService.ingestAll();
    }
}