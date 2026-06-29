# Car Sharing

A microservices car-sharing demo built with Quarkus, PostgreSQL and NATS JetStream.

## Architecture

Three services, each owning its own database, communicating via REST (sync) and NATS JetStream (async).

```
                    ┌──────────────┐
                    │   Frontend   │
                    │   (Angular)  │
                    └──────┬───────┘
                           │
        ┌──────────────────┼──────────────────┐
        │                  │                  │
        ▼                  ▼                  ▼
  ┌──────────┐      ┌────────────┐    ┌────────────┐
  │   user   │      │  vehicle   │    │  booking   │
  │ :8081    │      │  :8082     │    │  :8083     │
  └────┬─────┘      └─────┬──────┘    └──────┬─────┘
       │                  │   ▲              │
       │                  │   │ REST         │
       │                  │   └──────────────┤
       │                  │                  │
       │                  │  ◄── NATS ───────┤
       │                  │                  │
       ▼                  ▼                  ▼
   ┌────────┐         ┌─────────┐       ┌─────────┐
   │ userdb │         │vehicledb│       │bookingdb│
   └────────┘         └─────────┘       └─────────┘

       NATS JetStream (booking.created / .started / .ended / .cancelled)
```

### Services

- **user-service**: registration, login, JWT issuance. Owns user identity and roles.
- **vehicle-service**: vehicle catalog and status. Subscribes to booking events to update availability.
- **booking-service**: booking and managing rides. Calls vehicle-service synchronously for availability, emits NATS events for state propagation.

### Event Flow (NATS JetStream)

| Booking action | Event published | Vehicle status transition |
|---|---|---|
| Create | `booking.created` | `AVAILABLE → RESERVED` |
| Start ride | `booking.started` | `RESERVED → IN_USE` |
| End ride | `booking.ended` | `IN_USE → AVAILABLE` |
| Cancel | `booking.cancelled` | `RESERVED → AVAILABLE` |

## Stack

- **Quarkus 3.36** (Java 21)
- **PostgreSQL 16** - one instance, three databases
- **NATS JetStream** - durable messaging
- **Maven** - multi-module monorepo
- **Docker** + **Kubernetes (kind)** - local deployment
- **SmallRye JWT** - auth
- **Hibernate ORM + Panache** - persistence
- **Lombok** - DTO boilerplate
- **Bcrypt** - password hashing

## Running Locally

**Prerequisites:** JDK 21, Docker, Maven.

The JWT signing keys aren't committed to the repo
```bash
# Generate RSA key pair in PKCS8 format
openssl genrsa -out private.pem 2048
openssl rsa -in private.pem -pubout -out public.pem
openssl pkcs8 -topk8 -inform PEM -in private.pem -out private-pkcs8.pem -nocrypt

# Place keys in user-service
mv private-pkcs8.pem user-service/src/main/resources/private.pem
cp public.pem user-service/src/main/resources/public.pem

# Distribute public key to the other service
cp public.pem vehicle-service/src/main/resources/public.pem
mv public.pem booking-service/src/main/resources/public.pem

# Cleanup
rm private.pem
```

### 1. Start infrastructure

```bash
cd infrastructure
docker compose up -d
```

This brings up Postgres on `5432` and NATS on `4222`.

### 2. Run each service in dev mode

In three separate terminals:

```bash
cd user-service    && ../mvnw quarkus:dev   # :8081
cd vehicle-service && ../mvnw quarkus:dev   # :8082
cd booking-service && ../mvnw quarkus:dev   # :8083
```

### 3. Seeded credentials

On startup, user-service seeds:

- **admin@carsharing.com** / pw: `admin123` (role: ADMIN)
- **user@example.com** / pw: `user123` (role: USER)

vehicle-service seeds 4 vehicles via `import.sql`.

### 4. Swagger UI

Swagger UI is available for each service:

- `http://localhost:8081/q/swagger-ui`
- `http://localhost:8082/q/swagger-ui`
- `http://localhost:8083/q/swagger-ui`

## Running on Kubernetes

**Prerequisites:** Docker, `kind`, `kubectl`. Images are public on Docker Hub (`maubersek/car-sharing-*`).

#### 1. Create the cluster

```bash
kind create cluster --name carsharing
kubectl config set-context --current --namespace=carsharing
```

#### 2. Apply all manifests

```bash
kubectl apply -f infrastructure/k8s/
```

View status of pods:

```bash
kubectl get pods -w
```

#### 3. Port-forward to access the services

```bash
kubectl port-forward svc/user-service    9081:8081 &
kubectl port-forward svc/vehicle-service 9082:8082 &
kubectl port-forward svc/booking-service 9083:8083 &
```