# BillSafe

BillSafe is a backend application for managing purchases, warranties, and invoices. It provides secure REST APIs for purchase management, attachment storage, dashboard analytics, optimized data access using Redis caching, and event-driven warranty notifications using Kafka.

## Features

### Authentication

* JWT-based authentication
* Spring Security integration
* Secure REST APIs

### Purchase Management

* Create, update, delete, and retrieve purchases
* Dynamic filtering using JPA Specifications
* Pagination and sorting
* Warranty tracking

### Attachment Management

* Upload purchase invoices and warranty documents
* Download stored attachments
* Multiple attachments per purchase
* Local file storage with an abstraction that can be extended to cloud storage

### Dashboard

* Purchase statistics
* Warranty summary
* Recent purchases overview
* Redis caching for frequently accessed dashboard data

### Performance Optimization

* Manual Redis Cache-Aside implementation
* Generic reusable Redis cache service
* Purchase caching
* Dashboard caching
* Cache invalidation on data updates

### Notification Service

* Event-driven notification processing using Apache Kafka
* Publishes a `PurchaseCreatedEvent` when a purchase is created
* Kafka consumer processes purchase events asynchronously
* Creates warranty reminder notifications
* Notification status tracking using `PENDING`, `SENT`, and `FAILED`
* Scheduled processing for due warranty reminders
* Email notifications for upcoming warranty expiry

## Tech Stack

* Java 21
* Spring Boot 3
* Spring Security
* Spring Data JPA
* PostgreSQL
* Redis
* Apache Kafka
* JWT
* Maven
* Docker
* Docker Compose

## Architecture

```text
                         Client
                           │
                           ▼
                 Spring Boot REST API
                           │
              ┌────────────┼────────────┐
              ▼            ▼            ▼
         PostgreSQL      Redis        Kafka
                                       │
                                       ▼
                              Notification Service
                                       │
                                       ▼
                                  Email Service
```

### Core Request Flow

```text
Client
  │
  ▼
Controller
  │
  ▼
Service
  │
  ▼
Repository
  │
  ▼
PostgreSQL
```

### Notification Flow

```text
Purchase Created
       │
       ▼
PurchaseCreatedEvent
       │
       ▼
Kafka Producer
       │
       ▼
purchase-created Topic
       │
       ▼
Kafka Consumer
       │
       ▼
Notification Service
       │
       ▼
Notification (PENDING)
       │
       ▼
Scheduled Processing
       │
       ▼
Email Service
       │
       ▼
Notification (SENT / FAILED)
```

## Project Structure

```text
src/main/java
├── auth
├── purchase
├── attachment
├── dashboard
├── notification
├── cache
├── common
├── config
└── security
```

## Redis Caching

BillSafe implements a manual **Cache-Aside** caching strategy instead of relying on Spring's `@Cacheable`.

### Cache Flow

```text
Client
   │
   ▼
Redis Cache
   │
   ▼
Cache Hit?
   │
 ├── Yes → Return cached response
 │
 └── No
      │
      ▼
 PostgreSQL
      │
      ▼
 Store in Redis
      │
      ▼
 Return Response
```

### Cached Resources

* Purchase by ID
* Dashboard statistics

### Cache Invalidation

When cached data becomes outdated because of an update or deletion, the corresponding cache entry is evicted so that the next request retrieves fresh data from PostgreSQL.

## Kafka Notification System

BillSafe uses Kafka to decouple purchase management from notification processing.

When a purchase is created:

```text
Purchase Service
      │
      ▼
PurchaseCreatedEvent
      │
      ▼
Kafka
      │
      ▼
Notification Consumer
```

The consumer creates a pending warranty notification without requiring the purchase service to directly handle notification processing.

A scheduled task later checks for due notifications and sends the corresponding email.

This provides asynchronous processing and keeps notification logic separate from the core purchase workflow.

## REST API Features

* JWT Authentication
* Purchase CRUD Operations
* Attachment Upload & Download
* Dynamic Search & Filtering
* Pagination
* Sorting
* Dashboard Analytics
* Redis Caching
* Event-driven warranty notifications
* Scheduled warranty reminders
* Email notifications

## Running the Application

### Clone Repository

```bash
git clone <repository-url>
cd billsafe
```

### Start Redis

```bash
docker run -d --name redis -p 6379:6379 redis:8
```

### Start Kafka

Kafka is configured through Docker Compose.

```bash
docker compose up -d kafka
```

### Start PostgreSQL

Ensure PostgreSQL is running locally and update `application.yml` if necessary.

### Run Application

```bash
mvn spring-boot:run
```

## Docker

The application, Redis, and Kafka can be run using Docker Compose:

```bash
docker compose up --build
```

PostgreSQL can remain on the local development machine and be accessed from the application container using the configured database environment variables.

## Database Migrations

BillSafe uses **Flyway** to version and manage PostgreSQL schema changes.

Migration files are stored under:

```text
src/main/resources/db/migration
```

Example:

```text
V1__create_user_table.sql
V2__create_purchase_table.sql
V3__create_attachment_table.sql
V4__...
V5__create_notifications_table.sql
```

Hibernate is configured to validate the schema rather than automatically modify it.

```yaml
spring:
  jpa:
    hibernate:
      ddl-auto: validate
```

This keeps database schema changes explicit, version-controlled, and reproducible.

## Learning Highlights

This project demonstrates practical implementation of:

* Spring Security with JWT
* RESTful API Design
* Layered Backend Architecture
* Dynamic Queries using JPA Specifications
* Pagination and Sorting
* Redis Cache-Aside Pattern
* Generic Redis Cache Service
* Cache Invalidation Strategies
* File Storage Management
* Event-Driven Architecture with Kafka
* Kafka Producer and Consumer
* Scheduled Background Processing
* Email Notification Integration
* Database Versioning with Flyway
* Docker Containerization
* Docker Compose

## Future Enhancements

* Unit & Integration Testing
* Improved Kafka retry and failure handling
* CI/CD Pipeline
* Cloud Deployment
* AWS S3-based attachment storage
* Production monitoring and observability

## License

This project is intended for educational and portfolio purposes.
