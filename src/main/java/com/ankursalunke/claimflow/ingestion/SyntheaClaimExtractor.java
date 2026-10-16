package com.ankursalunke.claimflow.ingestion;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

@Component
@RequiredArgsConstructor
public class SyntheaClaimExtractor {
    private static final String URN_UUID_PREFIX = "urn:uuid:";
    private final ObjectMapper objectMapper;

    public List<ExtractedClaim> extract(JsonNode bundle) {
        return bundle.path("entry").values().stream()
                .map(entry -> entry.path("resource"))
                .filter(resource -> "Claim".equals(resource.path("resourceType").asString("")))
                .map(this::toExtractedClaim)
                .toList();
    }

    private ExtractedClaim toExtractedClaim(JsonNode claim){
        String claimId = claim.path("id").asString("");
        String reference = claim.path("patient").path("reference").asString("");
        String json = objectMapper.writeValueAsString(claim);
        return new ExtractedClaim(claimId, parsePatientId(reference), json);
    }

    static String parsePatientId(String reference){
        if (reference.startsWith(URN_UUID_PREFIX)){
            return reference.substring(URN_UUID_PREFIX.length());
        }
        return null;
    }

}
