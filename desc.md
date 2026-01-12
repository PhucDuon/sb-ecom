# 🎟️ Ticket: Xây dựng Hệ thống E-Commerce Full-Stack (Spring Boot Ecosystem)

## 📌 Overview

Xây dựng một hệ thống **E-Commerce quy mô lớn** sử dụng **Spring Boot** làm backend core, tích hợp nhiều hệ quản trị dữ liệu và công cụ xử lý realtime nhằm đáp ứng yêu cầu **hiệu năng cao, khả năng mở rộng và báo cáo nhanh**.

Hệ thống hỗ trợ **2 loại người dùng (User / Admin)**, có đầy đủ chức năng mua hàng, thanh toán, tìm kiếm nâng cao và báo cáo thống kê.

---

## 🧱 Tech Stack

* **Backend**: Spring Boot, Spring Security, Spring Data
* **Authentication**: JWT (Access Token + Refresh Token)
* **Databases**:

    * PostgreSQL – dữ liệu giao dịch (User, Order, Payment)
    * MongoDB – dữ liệu phi cấu trúc (Product detail, logs)
    * Redis – cache, session, rate limit
    * Elasticsearch – search & semantic search
* **CDC Tool**: Debezium (sync dữ liệu từ DB → Elasticsearch)
* **Payment**: Stripe
* **Performance Testing**: Apache JMeter

---

## 👤 User Roles

| Role      | Permissions                                                   |
| --------- | ------------------------------------------------------------- |
| **User**  | Xem sản phẩm, tìm kiếm, đặt hàng, thanh toán, xem lịch sử đơn |
| **Admin** | Quản lý sản phẩm, quản lý user, báo cáo                       |

---

## 🔐 Authentication & Authorization

* Sign In / Register / Logout
* JWT-based authentication
* Role-based authorization (USER / ADMIN)


---

## ✅ Functional Requirements

### 1️⃣ Authentication

* [ ] Sign In
* [ ] Register
* [ ] Logout

---

### 2️⃣ Products

* [ ] View product list
* [ ] View product detail by ID
* [ ] Search products by keyword (Elasticsearch)
* [ ] Semantic search (Elasticsearch vector / AI-based)
* [ ] Create product (Admin only)
* [ ] Update product (Admin only)
* [ ] Delete product (Admin only)
* [ ] Manage product price & inventory (stock)

---

### 3️⃣ Customers (Admin Only)

* [ ] View customer list
* [ ] View customer detail
* [ ] Activate / Inactivate user account

---

### 4️⃣ Orders & Payment

* [ ] Create order
* [ ] Integrate payment with **Stripe**
* [ ] Order status notification:

    * Order created successfully
    * Payment successful
* [ ] View order history (User)

---

### 5️⃣ Reporting (Admin Only)

* [ ] Revenue report

    * By day
    * By week
    * By month
    * By year
* [ ] User statistics

    * New users per day/week/month/year

---

## ⚙️ Non-Functional Requirements

### 📊 Performance & Load Testing

* Sử dụng **Apache JMeter** để đo lường hiệu năng
* Trước khi chạy test:

    * Import dữ liệu test vào DB
    * Product table > **500,000 records**
    * Order table > **1,000,000 records**

---

### 🚀 Performance Targets

* [ ] Hệ thống chịu tải **≥ 1000 QPS** (Query Per Second)
* [ ] Hệ thống đặt hàng đạt **≥ 300 TPS** (Transaction Per Second)
* [ ] Báo cáo tổng hợp có **Response Time < 1s**

---

## 📦 Deliverables

* Source code backend (Spring Boot)
* Database schema & migration scripts
* Elasticsearch index & mapping
* JMeter test plan & test report
* API documentation (Swagger / OpenAPI)

---

## 🧪 Acceptance Criteria

* Tất cả API được bảo vệ bằng JWT
* Role-based access hoạt động chính xác
* Search & semantic search trả kết quả chính xác
* Hệ thống đạt yêu cầu hiệu năng theo NFR
* Báo cáo trả về < 1s với dữ liệu lớn

---

Nếu bạn muốn, mình có thể:

* ✏️ Tách ticket này thành **Epic → Story → Task**
* 📐 Thiết kế **System Architecture Diagram**
* 🧪 Viết **JMeter test plan**
* 📄 Viết **API contract (OpenAPI spec)**
