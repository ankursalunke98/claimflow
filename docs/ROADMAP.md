# ClaimFlow Roadmap

## Today (MVP)
- Synthea generates synthetic patients + insurance claims
- Producer streams claims into Kafka (claims.raw, keyed by patient id)
- Consumer validates, upserts to Postgres; bad records → claims.dlq
- REST: GET /patients/{id}/claims
- README with architecture diagram

## Next 2 weeks
- Adjudication + fraud rules engine
- Observability: Prometheus + Grafana (lag, throughput, DLQ rate)
- Secure LLM layer: pgvector + Spring AI, permissioned retrieval over policy docs

## Parked
- Clinical-only platform, Open Food Facts, NAV Pulse
- Kubernetes, cloud deploy, Python