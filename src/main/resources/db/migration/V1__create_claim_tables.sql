CREATE TABLE claim (
    id                 UUID           PRIMARY KEY,
    patient_id         UUID           NOT NULL,
    status             VARCHAR(20)    NOT NULL,
    claim_type         VARCHAR(20)    NOT NULL,
    provider_id        VARCHAR(64),
    provider_name      VARCHAR(255),
    coverage           VARCHAR(100),
    billable_start     TIMESTAMPTZ    NOT NULL,
    billable_end       TIMESTAMPTZ,
    created_at_source  TIMESTAMPTZ    NOT NULL,
    total_amount       NUMERIC(12,2)  NOT NULL,
    currency           CHAR(3)        NOT NULL,
    raw_payload        JSONB          NOT NULL,
    ingested_at        TIMESTAMPTZ    NOT NULL DEFAULT now(),
    CONSTRAINT chk_claim_type CHECK (claim_type IN ('institutional', 'oral', 'pharmacy', 'professional', 'vision')),
    CONSTRAINT chk_total_non_negative CHECK (total_amount >= 0)
);

CREATE INDEX idx_claim_patient_billable ON claim (patient_id, billable_start DESC);

CREATE TABLE claim_item (
    claim_id       UUID           NOT NULL REFERENCES claim (id) ON DELETE CASCADE,
    sequence       INT            NOT NULL,
    code_system    VARCHAR(100),
    code           VARCHAR(50)    NOT NULL,
    description    VARCHAR(500),
    net_amount     NUMERIC(12,2),
    encounter_id   UUID,
    PRIMARY KEY (claim_id, sequence)
);