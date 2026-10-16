package com.ankursalunke.claimflow.ingestion;

public record ExtractedClaim(
        String claimId,
        String patientId,
        String json
) {
}
