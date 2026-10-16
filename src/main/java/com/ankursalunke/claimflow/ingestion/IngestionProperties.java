package com.ankursalunke.claimflow.ingestion;

import jakarta.validation.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "claimflow.ingestion")
public record IngestionProperties(
        @NotBlank String sourceDir,
        @NotBlank String topic
) {
}