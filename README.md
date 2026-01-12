# 🛒 E-Commerce Backend System

A scalable backend system for an e-commerce platform, focusing on **high-concurrency order processing**, **asynchronous payment handling**, and **clean system design** using modern backend technologies.

---

## 🚀 Features

### 🔐 Authentication & Authorization
- JWT-based authentication
- Role-based access control (USER, ADMIN)
- Stateless security with Spring Security
- Admin-only APIs protected using `@PreAuthorize`

---

### 📦 Product Management
- CRUD product management (Admin)
- Public product listing and search
- Full-text search using Elasticsearch
- Pagination & sorting
- Event-driven sync between PostgreSQL and Elasticsearch (RabbitMQ)

---

### 🧾 Order Processing (Concurrency Safe)
- Safe order placement under high concurrency
- Redis Distributed Lock (Redisson) to prevent overselling
- Order lifecycle:
  - PENDING_PAYMENT
  - PAID
  - CANCELLED

---

### 💳 Asynchronous Payment
- Payment handled asynchronously via RabbitMQ
- Order service publishes payment events
- Payment service consumes and processes payments
- Idempotent consumer design

---

### ⏱ Auto-Cancel Unpaid Orders
- Orders auto-cancelled after 15 minutes if unpaid
- Implemented using RabbitMQ TTL & Dead Letter Queue
- Stock rollback handled safely with Redis lock

---

### 📊 Admin Reporting
- Revenue & order statistics
- Paid vs cancelled orders
- Low-stock product alerts
- New user statistics (day / month / year)

---

## 🏗 System Architecture

Client  
⬇  
Spring Boot Backend  
- Auth Module  
- Product Module (PostgreSQL + Elasticsearch)  
- Order Module (Redis Lock)  
- Payment Module (Async)  
⬇  
RabbitMQ  

---

## 🛠 Tech Stack
- Java 17
- Spring Boot
- Spring Security + JWT
- PostgreSQL
- Redis (Redisson)
- RabbitMQ
- Elasticsearch
- Spring Data JPA
- Maven

---

## 📁 Project Structure

```
src/main/java/com/ecommerce/project
├── auth
├── product
├── order
├── reporting
├── messaging
└── config
```

---

## 🎯 Key Design Decisions
- Redis distributed locking for concurrency safety
- Event-driven architecture
- Idempotent message consumers
- TTL + DLQ instead of cron jobs for order timeout

---

## ▶ How to Run

### Prerequisites
- Java 17+
- PostgreSQL
- Redis
- RabbitMQ
- Elasticsearch

### Run Application
```
mvn clean install
mvn spring-boot:run
```

---

## 🔌 Sample APIs

**Public**
- GET `/api/products`
- GET `/api/products/search`

**User**
- POST `/api/orders`
- GET `/api/orders/my`

**Admin**
- POST `/api/admin/products`
- GET `/api/admin/reports`

---

## 🧪 Testing
- API testing with Postman
- Concurrency testing using JMeter
- Verified race-condition safety under load

---

## 🔮 Future Improvements
- Integrate Stripe / PayPal
- Saga pattern for distributed transactions
- Docker & Kubernetes
- CI/CD pipeline

---

## 👤 Author
**Minh Phuc Duong**  
Java Backend Developer
