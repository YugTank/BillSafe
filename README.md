# BillSafe

BillSafe is a backend application for managing purchases, warranties, invoices, and related documents.

It provides secure REST APIs for purchase management, AWS S3-based attachment storage, dashboard analytics, Redis caching, and event-driven warranty notifications using Apache Kafka.

---

## Features

### Authentication

- JWT-based authentication
- Spring Security integration
- Secure REST APIs

### Purchase Management

- Create, update, delete, and retrieve purchases
- Dynamic filtering using JPA Specifications
- Pagination and sorting
- Warranty tracking

### Attachment Management

- Upload purchase invoices and warranty documents
- Multiple attachments per purchase
- AWS S3-based file storage
- Download attachments directly from S3
- Delete attachments from S3
- Original file names preserved during download
- Storage abstraction for flexible storage implementations

### Dashboard

- Purchase statistics
- Warranty summary
- Recent purchases overview
- Redis caching for frequently accessed dashboard data

### Performance Optimization

BillSafe implements a manual **Cache-Aside** strategy instead of relying on Spring's `@Cacheable`.

- Generic reusable Redis cache service
- Purchase caching
- Dashboard caching
- Cache invalidation on data updates

### Notification System

- Event-driven notification processing using Apache Kafka
- Publishes `PurchaseCreatedEvent` when a purchase is created
- Kafka consumer processes purchase events asynchronously
- Creates warranty reminder notifications
- Scheduled processing of due notifications
- Email notifications for upcoming warranty expiry
- Notification status tracking using:
  - `PENDING`
  - `SENT`
  - `FAILED`
- Failed notification retry handling
- Maximum retry limit to prevent indefinite retries

---

## Tech Stack

- Java 21
- Spring Boot 3
- Spring Security
- Spring Data JPA
- PostgreSQL
- Flyway
- Redis
- Apache Kafka
- AWS S3
- JWT
- Maven
- Docker
- Docker Compose

---

## Architecture

```text
                         Client
                           │
                           ▼
                 Spring Boot REST API
                           │
              ┌────────────┼─────────────┐
              ▼            ▼             ▼
         PostgreSQL      Redis         Kafka
              │                          │
              │                          ▼
              │                  Notification Service
              │                          │
              │                          ▼
              │                    Email Service
              │
              ▼
       Purchase / Attachment
              │
              ▼
           AWS S3
````

---

## Core Request Flow

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

---

## Attachment Storage Flow

BillSafe uses AWS S3 for persistent attachment storage.

```text
Client
   │
   ▼
MultipartFile
   │
   ▼
AttachmentService
   │
   ▼
StorageService
   │
   ▼
S3StorageService
   │
   ▼
AWS S3
```

The S3 object key is stored in PostgreSQL along with attachment metadata.

```text
PostgreSQL
┌────────────────────────────────────────────┐
│ Attachment                                 │
├────────────────────────────────────────────┤
│ id                                         │
│ file_name                                  │
│ file_type                                  │
│ file_path → S3 object key                  │
│ purchase_id                                │
│ uploaded_at                                │
└────────────────────────────────────────────┘

AWS S3
┌────────────────────────────────────────────┐
│ bills/                                     │
│   └── generated-file-key.pdf               │
│                                            │
│ warranty/                                  │
│   └── generated-file-key.pdf               │
└────────────────────────────────────────────┘
```

### Storage Abstraction

The application uses a `StorageService` abstraction:

```text
StorageService
      │
      └── S3StorageService
              │
              ├── upload()
              ├── download()
              └── delete()
```

This keeps attachment business logic independent of the underlying storage implementation.

---

## Redis Caching

BillSafe implements a manual **Cache-Aside** caching strategy.

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

When cached data becomes outdated because of an update or deletion, the corresponding cache entry is evicted.

The next request retrieves fresh data from PostgreSQL and repopulates the cache.

---

## Kafka Notification System

BillSafe uses Apache Kafka to decouple purchase management from warranty notification processing.

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
       ├── Success → SENT
       │
       └── Failure → FAILED
                         │
                         ▼
                       Retry
                         │
                  Retry Limit Reached
                         │
                         ▼
                       Stop
```

### Why Kafka?

Kafka keeps notification processing separate from the purchase creation workflow.

When a purchase is created, the purchase service publishes an event instead of directly handling email notification logic.

The notification consumer processes the event asynchronously and creates a pending warranty reminder.

This provides:

* Decoupled notification processing
* Asynchronous event handling
* Separation of concerns
* Better extensibility for additional notification channels

---

## Scheduled Warranty Reminders

A scheduled background task checks for due notifications.

```text
Scheduler
    │
    ▼
Find PENDING / FAILED notifications
    │
    ▼
Check retry limit
    │
    ▼
Send Email
    │
 ┌──┴───────────┐
 ▼              ▼
Success       Failure
 │              │
 ▼              ▼
SENT          FAILED
                │
                ▼
             retryCount++
```

The scheduler runs daily and processes notifications that are due.

---

## Project Structure

```text
src/main/java
├── auth
├── purchase
├── attachment
├── notification
├── dashboard
├── cache
├── storage
├── common
├── config
└── security
```

---

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
V6__...
```

Hibernate is configured to validate the schema rather than automatically modify it:

```yaml
spring:
  jpa:
    hibernate:
      ddl-auto: validate
```

This keeps database schema changes:

* Explicit
* Version-controlled
* Reproducible

---

## REST API Features

* JWT Authentication
* Purchase CRUD Operations
* Dynamic Search & Filtering
* Pagination
* Sorting
* Dashboard Analytics
* Redis Caching
* Attachment Upload
* Attachment Download
* Attachment Delete
* AWS S3 File Storage
* Event-driven Warranty Notifications
* Scheduled Warranty Reminders
* Email Notifications
* Failed Notification Retry Handling

---

## Running the Application

### Clone Repository

```bash
git clone https://github.com/YugTank/BillSafe.git
cd BillSafe
```

### Environment Variables

BillSafe requires environment variables for database, Kafka, Redis, email, and AWS S3 configuration.

Create a local `.env` file:

```env
AWS_ACCESS_KEY_ID=your-access-key
AWS_SECRET_ACCESS_KEY=your-secret-key
AWS_REGION=your-aws-region
AWS_S3_BUCKET=your-bucket-name
```

Do **not** commit `.env` to Git.

Make sure it is included in `.gitignore`:

```gitignore
.env
```

### PostgreSQL

Ensure PostgreSQL is running locally and configure the required database environment variables.

### Redis

Start Redis:

```bash
docker run -d --name billsafe-redis -p 6379:6379 redis:latest
```

### Kafka

Kafka is configured through Docker Compose:

```bash
docker compose up -d kafka
```

### Run Application

```bash
mvn spring-boot:run
```

---

## Docker

BillSafe can be run using Docker Compose:

```bash
docker compose up --build
```

The Dockerized application uses:

* PostgreSQL
* Redis
* Kafka
* AWS S3

PostgreSQL can remain on the local development machine and be accessed from the application container through the configured database environment variables.

AWS credentials and configuration should be supplied to the container through environment variables.

---

## AWS S3 Configuration

BillSafe uses AWS S3 for attachment storage.

Required environment variables:

```text
AWS_ACCESS_KEY_ID
AWS_SECRET_ACCESS_KEY
AWS_REGION
AWS_S3_BUCKET
```

The AWS SDK uses the configured credentials to access the S3 bucket.

Required S3 operations include:

```text
s3:ListBucket
s3:GetObject
s3:PutObject
s3:DeleteObject
```

Bucket-level and object-level permissions should be configured separately according to AWS IAM policies.

---

## Key Design Decisions

### Manual Redis Cache-Aside

Instead of relying on Spring's `@Cacheable`, BillSafe implements caching explicitly to understand and control:

* Cache reads
* Cache population
* Cache invalidation
* Serialization
* Cache key management

### Kafka for Notifications

Kafka separates purchase creation from notification processing.

The purchase workflow does not need to directly send emails.

### Storage Abstraction

Attachments are handled through a `StorageService` abstraction, allowing the business layer to remain independent from the storage provider.

The current implementation uses AWS S3.

### Flyway for Database Versioning

Database changes are managed through versioned SQL migrations instead of relying on automatic Hibernate schema generation.

---

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
* AWS S3 Object Storage
* AWS IAM Permissions
* Storage Abstraction
* Event-Driven Architecture with Kafka
* Kafka Producer and Consumer
* Scheduled Background Processing
* Email Notification Integration
* Notification Retry Handling
* Database Versioning with Flyway
* Docker Containerization
* Docker Compose

---

## Future Enhancements

* Unit & Integration Testing
* Improved Kafka failure handling
* CI/CD Pipeline
* Cloud Deployment
* Production monitoring and observability
* Improved notification retry/dead-letter handling
* Presigned S3 URLs for direct client downloads

---

## License

This project is intended for educational and portfolio purposes.

