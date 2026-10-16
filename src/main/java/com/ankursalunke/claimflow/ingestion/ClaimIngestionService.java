package com.ankursalunke.claimflow.ingestion;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.io.File;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClaimIngestionService {

    private final IngestionProperties properties;
    private final SyntheaClaimExtractor extractor;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public IngestionReport ingestAll() {
        long start = System.currentTimeMillis();
        int filesProcessed = 0;
        int filesFailed = 0;
        int claimsPublished = 0;
        int claimsFailed = 0;

        // 1. the data folder
        File folder = new File(properties.sourceDir());
        for (File file : folder.listFiles()) {
            String name = file.getName();

            // 2. skip anything that is not a patient file
            if (!name.endsWith(".json")
                    || name.startsWith("hospitalInformation")
                    || name.startsWith("practitionerInformation")) {
                continue;
            }

            // 3. open the envelope: text file -> tree
            JsonNode bundle;
            try {
                bundle = objectMapper.readTree(file);
            } catch (JacksonException e) {
                log.error("Skipping broken file {}", name);
                filesFailed++;
                continue;
            }
            filesProcessed++;

            // 4. pull out the bills, 5. send each one to Kafka and wait for "got it"
            for (ExtractedClaim claim : extractor.extract(bundle)) {
                try {
                    kafkaTemplate.send(properties.topic(), claim.patientId(), claim.json()).join();
                    claimsPublished++;
                } catch (Exception e) {
                    log.error("Failed to send claim {}", claim.claimId());
                    claimsFailed++;
                }
            }
        }

        // 6. hand in the summary slip
        long durationMs = System.currentTimeMillis() - start;
        IngestionReport report = new IngestionReport(
                filesProcessed, filesFailed, claimsPublished, claimsFailed, durationMs);
        log.info("Ingestion finished: {}", report);
        return report;
    }
}