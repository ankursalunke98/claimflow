package com.ankursalunke.claimflow.ingestion;

public record IngestionReport(
        int filesProcessed,
        int filesFailed,
        int claimsPublished,
        int claimsFailed,
        long durationMs
) {
}
