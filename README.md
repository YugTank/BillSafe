# BillSafe

BillSafe is a backend application for managing purchases, warranties, and invoices. It provides secure REST APIs for purchase management, attachment storage, dashboard analytics, and optimized data access using Redis caching.

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
- Download stored attachments
- Multiple attachments per purchase

### Dashboard
- Purchase statistics
- Warranty summary
- Recent purchases overview

### Performance Optimization
- Manual Redis Cache-Aside implementation
- Generic reusable Redis cache service
- Dashboard caching
- Cache invalidation on data updates

---

## Tech Stack

- Java 21
- Spring Boot 3
- Spring Security
- Spring Data JPA
- PostgreSQL
- Redis
- JWT
- Maven
- Docker
- Docker Compose

---

## Architecture

```
                Client
                  │
                  ▼
        Spring Boot REST API
                  │
       ┌──────────┴──────────┐
       ▼                     ▼
 PostgreSQL               Redis
```

---

## Project Structure

```
src/main/java
├── auth
├── purchase
├── attachment
├── dashboard
├── cache
├── common
├── config
└── security
```

---

## Redis Caching

BillSafe implements a manual **Cache-Aside** caching strategy instead of relying on Spring's `@Cacheable`.

### Cache Flow

```
Client
   │
   ▼
Redis Cache

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

- Purchase by ID
- Dashboard statistics

---

## REST API Features

- JWT Authentication
- Purchase CRUD Operations
- Attachment Upload & Download
- Dynamic Search & Filtering
- Pagination
- Sorting
- Dashboard Analytics
- Redis Caching

---

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

### Start PostgreSQL

Ensure PostgreSQL is running locally and update `application.yml` if necessary.

### Run Application

```bash
mvn spring-boot:run
```

---

## Docker

Build and run the application using Docker:

```bash
docker compose up --build
```

---

## Future Enhancements

- Kafka-based notification service
- Warranty reminder scheduling
- Email notifications
- Unit & Integration Testing
- CI/CD Pipeline
- Cloud Deployment

---

## Learning Highlights

This project demonstrates practical implementation of:

- Spring Security with JWT
- RESTful API Design
- Redis Cache-Aside Pattern
- Generic Redis Cache Service
- Cache Invalidation Strategies
- File Storage Management
- Dynamic Queries using JPA Specifications
- Docker Containerization

---

## License

This project is intended for educational and portfolio purposes.
