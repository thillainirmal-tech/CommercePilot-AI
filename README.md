# CommercePilot AI

[![CI](https://github.com/thillainirmal-tech/CommercePilot-AI/actions/workflows/ci.yml/badge.svg)](https://github.com/thillainirmal-tech/CommercePilot-AI/actions/workflows/ci.yml)

An explainable, safety-first agentic commerce orchestrator. Inventory, risk, pricing and fulfillment agents produce an auditable order plan without silently performing irreversible operations.

## Why it stands out

- Deterministic multi-agent trace with evidence for every decision
- Human review for high-risk orders
- Inventory and pricing rules with reproducible behavior
- Explicit adapter boundary before payment, reservation or shipment writes
- Browser dashboard, validation, Actuator health, tests, CI and Docker

## Run

```bash
mvn spring-boot:run
```

Open `http://localhost:8088` or call `GET /api/orders/demo`. No API key or external service is required.

## Verify

```bash
mvn verify
docker build -t commercepilot-ai .
```

## Architecture

`Order request → inventory agent → risk agent → pricing agent → fulfillment plan → approval boundary`

The project intentionally generates plans only. Production payment, stock and delivery systems belong behind authenticated, idempotent adapters with explicit approval policies.

## License

MIT

