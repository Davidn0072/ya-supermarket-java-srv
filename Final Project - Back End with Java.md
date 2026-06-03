# Final Project — Online Supermarket Application (Back End with Java)

> **AI Instructions:** Build a complete microservices back-end for an online supermarket using Spring Boot. Follow every section below precisely. Do not add features beyond what is specified. Implement services one at a time, verifying each compiles and its tests pass before moving to the next.

Do NOT proceed to next phase unless explicitly instructed.
Each checkbox represents a task that must be completed before moving forward.
Do not Make Commit to Git hub By yourself
At the end of each phase — update all checkboxes in this document to reflect what was completed.

---

## Phase 0 — Environment Setup

- [x] Java 17 (JDK) installed — `java -version`
- [x] Maven 3.9.14 installed — `mvn -version`
- [x] Docker 29.3.1 installed and running — `docker -version`
- [x] IntelliJ IDEA installed and project opened successfully
- [x] Git installed and GitHub repo created — https://github.com/Davidn0072/ya-supermarket-java-srv

---

## Phase 1 — Project Skeleton

- [x] Root `pom.xml` (aggregator) created with all modules declared
- [x] Folder structure created as specified below
- [x] Each service has its own `pom.xml` with correct parent reference
- [x] `mvn validate` → BUILD SUCCESS (all 8 modules recognized)
- [x] Project opens in IntelliJ without errors

---

## Phase 2 — auth-service ✅

- [x] `AppUser` entity created (`id`, `username`, `passwordHash`, `roles`)
- [x] Postgres connected and schema created via JPA
- [x] BCrypt password encoding configured
- [x] `POST /api/auth/login` → returns JWT in HttpOnly cookie
- [x] `POST /api/auth/logout` → clears cookie
- [x] Unit test (Mockito) passes — 4 tests
- [x] Slice test (`@WebMvcTest`) passes — 2 tests
- [x] Integration test (`@SpringBootTest + H2`) passes — 4 tests
- [ ] JaCoCo coverage ≥ 60% (יאומת ב-`mvn verify`)
- [x] Service runs on port **8085**

---

## Phase 3 — product-service ✅

- [x] `Product` entity created with all fields (`sku` unique constraint)
- [x] `Category` entity created
- [x] Postgres connected
- [x] `GET /api/products` and `GET /api/products/{id}` implemented
- [x] `POST /api/products` (ADMIN only) — publishes `ProductCreatedEvent` to Kafka
- [x] `PUT /api/products/{id}` (ADMIN only)
- [x] `DELETE /api/products/{id}` (ADMIN only)
- [x] `GET /api/categories` implemented
- [x] Kafka producer configured and publishing to `product-events`
- [x] Unit test passes — 4 tests
- [x] Slice test passes — 2 tests
- [x] Integration test passes — 4 tests
- [ ] JaCoCo coverage ≥ 60% (יאומת ב-`mvn verify`)
- [x] Service runs on port **8080**

---

## Phase 4 — order-service ✅

- [x] `Order` entity created
- [x] `OrderItem` entity created (with price snapshot)
- [x] `Order` → `OrderItem` cascade relation configured
- [x] Postgres connected
- [x] `POST /api/orders` — validates stock, decrements `stockQuantity` (calls product-service), calculates total
- [x] `GET /api/orders/{id}` — owner or ADMIN only (`@orderAuthz.canRead`)
- [x] `GET /api/orders` — ADMIN only
- [x] `PATCH /api/orders/{id}/status` — ADMIN only
- [x] Publishes `OrderCreatedEvent` to Kafka `order-events`
- [x] Unit test passes — 4 tests
- [x] Slice test passes — 2 tests
- [x] Integration test passes — 4 tests
- [ ] JaCoCo coverage ≥ 60% (יאומת ב-`mvn verify`)
- [x] Service runs on port **8081**

---

## Phase 5 — customer-service ✅

- [x] `Customer` entity created (separate from `AppUser`)
- [x] Postgres connected
- [x] `GET /api/customers/{id}` — owner or ADMIN only (`@customerAuthz.canAccess`)
- [x] `PUT /api/customers/{id}` — update profile (address, phone)
- [x] Unit test passes — 3 tests
- [x] Slice test passes — 2 tests
- [x] Integration test passes — 4 tests
- [ ] JaCoCo coverage ≥ 60% (יאומת ב-`mvn verify`)
- [x] Service runs on port **8082**

---

## Phase 6 — search-service ✅

- [x] Elasticsearch connected
- [x] Kafka consumer configured for `product-events`
- [x] Kafka consumer configured for `order-events`
- [x] Products indexed on CREATE / UPDATE / DELETE events
- [x] `GET /api/search/products?q=...` returns results from Elasticsearch
- [x] Unit test passes — 3 tests
- [x] Slice test passes — 2 tests
- [x] Integration test passes — 2 tests
- [x] Service runs on port **8083**

---

## Phase 7 — image-service ✅

- [x] S3 bucket configured (credentials in application.properties)
- [x] `POST /api/images/upload` — uploads image, returns URL
- [x] Unit test passes — 2 tests
- [x] Slice test passes — 1 test
- [x] Service runs on port **8084**

---

## Phase 8 — api-gateway ✅

- [x] Spring Cloud Gateway configured
- [x] Route per service defined (6 routes in application.yml)
- [x] `JwtGatewayFilter` validates `ACCESS_TOKEN` cookie on all protected routes
- [x] Public routes bypass JWT validation:
  - [x] `GET /api/products/**`
  - [x] `GET /api/categories/**`
  - [x] `GET /api/search/**`
  - [x] `POST /api/auth/login`
  - [x] `POST /api/auth/logout`
- [x] 401 returned for missing/invalid token
- [x] Unit test passes — 4 tests
- [x] Integration test passes — 3 tests
- [x] Gateway runs on port **8000**

---

## Phase 9 — Authorization (`@PreAuthorize`)

- [ ] `@EnableMethodSecurity` enabled in each service
- [ ] `POST /api/orders` — `isAuthenticated()`, `customerId` from `Authentication` object
- [ ] `GET /api/orders/{id}` — `@orderAuthz.canRead(#id)` (owner or ADMIN)
- [ ] `GET /api/orders` — `hasRole('ADMIN')`
- [ ] `POST/PUT/DELETE /api/products` — `hasRole('ADMIN')`
- [ ] `PATCH /api/orders/{id}/status` — `hasRole('ADMIN')`
- [ ] `AccessDeniedException` handler returns 403

---

## Phase 10 — Docker

- [ ] Multi-stage `Dockerfile` for each service
- [ ] `docker-compose.yml` includes: all services + Postgres + Kafka + Elasticsearch
- [ ] `docker compose up -d` brings up entire stack successfully

---

## Phase 11 — Kubernetes

- [ ] `k8s/00-namespace.yaml`
- [ ] `k8s/10-postgres.yaml`
- [ ] `k8s/20-secrets.yaml`
- [ ] `k8s/30-auth-service.yaml`
- [ ] `k8s/40-product-service.yaml`
- [ ] `k8s/50-order-service.yaml`
- [ ] `k8s/99-api-gateway.yaml`
- [ ] At least one service has `replicas: 2` or more
- [ ] `kubectl apply -f k8s/` deploys successfully

---

## Phase 12 — End-to-End Flows (Postman)

- [ ] **Flow 1** — Guest browsing: products list, search, 401 on order
- [ ] **Flow 2** — Customer places order: login, create order, verify Kafka log, 403 for other user
- [ ] **Flow 3** — Admin manages inventory: login, create product, update status, delete product
- [ ] **Flow 4** — Defense in depth: 401 at gateway, 403 at service
- [ ] **Flow 5** — Kubernetes self-healing: delete pod, verify recreation
- [ ] Postman collection saved as `postman-collection.json` in repo root
- [ ] Every request has a short description

---

## Phase 13 — Submission

- [ ] `README.md` written with run/test/deploy instructions
- [ ] All code pushed to GitHub (manually by user)
- [ ] Link sent to **devprojects2000@gmail.com**
- [ ] Subject: `[Course No] - Java Final Project [Full Name]`

---

## Reference

### Domain Overview
An online supermarket where users can browse products, add them to a cart, and place orders. Administrators manage inventory.

### Entities

| Entity | Key Fields | Notes |
|--------|-----------|-------|
| `Product` | `id`, `sku` (unique), `name`, `description`, `price`, `stockQuantity`, `categoryId`, `imageUrl` | Product listed in the catalog |
| `Category` | `id`, `name` | e.g. Dairy, Bakery, Produce |
| `Customer` | `id`, `username`, `passwordHash`, `email`, `address`, `roles` | End user (domain entity) |
| `Order` | `id`, `customerId`, `status` (`PENDING / CONFIRMED / SHIPPED / DELIVERED`), `totalPrice`, `createdAt` | An order |
| `OrderItem` | `id`, `orderId`, `productId`, `quantity`, `priceAtPurchase` | Snapshot of product price at purchase time |
| `AppUser` | `id`, `username`, `passwordHash`, `roles` | Auth identity (`USER / ADMIN`). **Separate from Customer** |

### Architecture

```
                       ┌─────────────────────┐
                       │    API Gateway       │  port 8000
                       │  + JWT validation    │
                       └─────────────────────┘
                                 │
        ┌──────────┬─────────┬──────────┬──────────┬──────────┐
        │          │         │          │          │          │
   product      order    customer     auth      search    image
   service      service  service    service    service   service
   (8080)       (8081)   (8082)     (8085)     (8083)    (8084)
   Postgres     Postgres Postgres  Postgres  Elasticsearch  S3
        │
        └── publish ──► Kafka (order-events) ──► search-service
                                              └► email-notifier (optional)
```

### Kafka Topics

| Topic | Producer | Consumer |
|-------|---------|---------|
| `product-events` | `product-service` (CREATE / UPDATE / DELETE) | `search-service` |
| `order-events` | `order-service` (CREATE / STATUS_CHANGE) | `search-service`, optionally `email-notifier` |

### Folder Structure

```
supermarket/
├── pom.xml
├── docker-compose.yml
├── README.md
├── .github/workflows/ci.yml
├── postman-collection.json
├── api-gateway/
├── auth-service/
├── product-service/
├── order-service/
├── customer-service/
├── search-service/
├── image-service/
└── k8s/
    ├── 00-namespace.yaml
    ├── 10-postgres.yaml
    ├── 20-secrets.yaml
    ├── 30-auth-service.yaml
    ├── 40-product-service.yaml
    ├── 50-order-service.yaml
    └── 99-api-gateway.yaml
```
