# Online Supermarket — Back End with Java

A complete microservices back-end for an online supermarket, built with Spring Boot, Kafka, Elasticsearch, and Kubernetes.

---

## Architecture

```
                       ┌─────────────────────┐
                       │    API Gateway       │  :8000
                       │  + JWT validation    │
                       └─────────────────────┘
                                 │
        ┌──────────┬─────────┬──────────┬──────────┬──────────┐
        │          │         │          │          │          │
   product      order    customer     auth      search    image
   service      service  service    service    service   service
   :8080        :8081    :8082      :8085      :8083     :8084
   Postgres     Postgres Postgres  Postgres  Elastic-   S3
                                              search
        │
        └── Kafka (product-events, order-events) ──► search-service
```

## Services

| Service | Port | Responsibility |
|---------|------|---------------|
| api-gateway | 8000 | Single entry point, JWT validation |
| auth-service | 8085 | Login / logout / JWT issuing |
| product-service | 8080 | Product & category catalog, Kafka producer |
| order-service | 8081 | Order creation, stock management, Kafka producer |
| customer-service | 8082 | Customer profile |
| search-service | 8083 | Kafka consumer, Elasticsearch indexing & search |
| image-service | 8084 | Product image upload to S3 |

## Default Users (seeded on startup)

| Username | Password | Role |
|----------|----------|------|
| user | password | USER |
| admin | admin | USER + ADMIN |

---

## How to Run Locally

**Prerequisites:** Docker Desktop running.

```bash
# 1. Clone the repository
git clone https://github.com/Davidn0072/ya-supermarket-java-srv.git
cd ya-supermarket-java-srv

# 2. Start the entire stack
docker compose up -d

# 3. Wait ~60 seconds for all services to start, then test:
curl http://localhost:8000/api/products
```

All services are available through the gateway at `http://localhost:8000`.

---

## How to Run Tests

Run tests for all services:
```bash
mvn test
```

Run tests for a single service:
```bash
mvn -pl auth-service test
mvn -pl product-service test
mvn -pl order-service test
mvn -pl customer-service test
mvn -pl search-service test
mvn -pl image-service test
mvn -pl api-gateway test
```

Run tests with coverage report:
```bash
mvn verify
# Report: target/site/jacoco/index.html
```

---

## How to Deploy to Kubernetes

**Prerequisites:** kubectl + a running cluster (Docker Desktop Kubernetes or Minikube).

```bash
# 1. Build Docker images
docker compose build

# 2. Apply all manifests in order
kubectl apply -f k8s/

# 3. Verify pods are running
kubectl get pods -n supermarket

# 4. Access the gateway
# NodePort: http://localhost:30000/api/products
```

---

## Key Design Decisions

- **JWT in HttpOnly cookies** — prevents XSS token theft. Validated at the gateway (layer 1) and at each service via `@PreAuthorize` (layer 2).
- **Kafka for decoupling** — product and order events are published to Kafka. search-service consumes them asynchronously, so a search-service crash does not affect the core order flow.
- **Price snapshot in OrderItem** — `priceAtPurchase` stores the price at the time of purchase, so changing a product's price later does not break existing orders.
- **Separate AppUser vs Customer** — `AppUser` (auth identity) is managed by auth-service. `Customer` (domain profile) is managed by customer-service. Avoids mixing authentication concerns with business domain.
- **order-service replicas: 2** — demonstrates Kubernetes self-healing. If one pod is deleted, the other continues serving requests.

---

## Postman Collection

Import `postman-collection.json` into Postman to run all 5 end-to-end flows:

1. **Flow 1** — Guest browsing (no login required)
2. **Flow 2** — Customer places an order
3. **Flow 3** — Admin manages inventory
4. **Flow 4** — Defense in depth (401 at gateway, 403 at service)
5. **Flow 5** — Kubernetes self-healing (kubectl commands)

Set the `baseUrl` variable to `http://localhost:8000` before running.
