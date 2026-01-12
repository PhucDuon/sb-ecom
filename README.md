🛒 E-Commerce Backend System

A scalable backend system for an e-commerce platform, focusing on high concurrency order processing, asynchronous payment handling, and robust system design using modern backend technologies.

📌 Features Overview
🔐 Authentication & Authorization

JWT-based authentication

Role-based access control:

USER: browse products, place orders, view own orders

ADMIN: manage products, view reports, create admin accounts

Stateless security using Spring Security + JWT filter

🛍 Product Management

CRUD operations for products (Admin only)

Product attributes:

Name, description, price, stock, active status

Public product search and listing

Elasticsearch integration for:

Full-text search

Filtering by price range

Pagination & sorting

Event-driven sync between PostgreSQL and Elasticsearch using RabbitMQ

🧾 Order Processing (High Concurrency Safe)

Users can place orders concurrently without overselling

Redis Distributed Lock (Redisson) used to:

Lock product stock per product ID

Prevent race conditions under high load

Order lifecycle:

PENDING_PAYMENT

PAID

CANCELLED

💳 Asynchronous Payment System

Payment requests are processed asynchronously via RabbitMQ

Order service publishes payment request events

Payment service:

Handles payment logic

Ensures idempotent processing

Payment results are sent back to order service via events

⏱ Auto-Cancel Unpaid Orders (TTL + DLQ)

Orders unpaid after 15 minutes are automatically cancelled

Implemented using RabbitMQ TTL + Dead Letter Queue

Flow:

Order created → message sent to delay queue

TTL expires → message routed to DLQ

Order is checked and auto-cancelled if still unpaid

Stock is rolled back safely using Redis lock

📊 Admin Reporting Module

(Admin-only endpoints)

Overview dashboard:

Total orders

Total revenue

Paid orders

Failed payments

Revenue reports grouped by month

Low-stock product alerts

New users statistics:

Per day / week / month / year
