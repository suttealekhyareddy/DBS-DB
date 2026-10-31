
# CampusCare — AI-Powered Campus Complaint & Service Management

Full-stack hackathon starter aligned to CODESPARK Hackathon 2026 Problem Statement 20.

## Stack
- Frontend: React + Vite
- Primary backend: Java 21 + Spring Boot + Spring Data JPA
- Database: MySQL 8
- Supporting AI service: Python + FastAPI (optional)
- Authentication: JWT + BCrypt, role-based authorization

> Java Spring Boot is the primary application backend. Python is only a supporting AI recommendation service.

## Included modules
- Student/faculty registration and login
- Admin-created staff accounts / faculty approval state
- Role-based dashboards and protected API routes
- Complaint registration, categories, priority, campus location
- Department and staff assignment
- Status workflow and status history
- Resolution notes, attachments metadata, feedback
- SLA deadline and overdue escalation endpoint
- Admin analytics, CSV export
- QR location prefill support (location code in URL)
- AI complaint category/priority/department recommendations
- Audit log entity and notification entity foundations

This is a runnable MVP foundation. Email delivery, production-grade file storage, real campus maps, and institution SSO need credentials/institution configuration and are intentionally represented by safe extension points.

## Prerequisites
- Java 21
- Maven 3.9+
- Node.js 20+
- MySQL 8
- Python 3.10+ (optional)

## 1. Database
Create database and user (or use your own credentials):

```sql
CREATE DATABASE campuscare;
CREATE USER 'campuscare'@'localhost' IDENTIFIED BY 'CampusCare@123';
GRANT ALL PRIVILEGES ON campuscare.* TO 'campuscare'@'localhost';
FLUSH PRIVILEGES;
```

Update `backend/src/main/resources/application.yml` if needed. JPA creates/updates MVP tables automatically. `database/schema.sql` documents the intended relational schema and indexes.

## 2. Start backend
```bash
cd backend
mvn spring-boot:run
```
Backend: http://localhost:8080  
Swagger/OpenAPI: http://localhost:8080/swagger-ui.html

First admin: create it using the registration endpoint only in a local demo, then promote the user in MySQL:
```sql
UPDATE users SET role='ADMIN', approved=true WHERE email='admin@campuscare.local';
```
For a real deployment, seed the admin through a controlled migration/secret, not public registration.

## 3. Start frontend
```bash
cd frontend
npm install
npm run dev
```
Frontend: http://localhost:5173

## 4. Optional AI service
```bash
cd ai-service
python -m venv .venv
# Windows: .venv\Scripts\activate
# Linux/macOS: source .venv/bin/activate
pip install -r requirements.txt
uvicorn app:app --reload --port 8001
```
AI endpoint: `POST http://localhost:8001/predict` with `{"description":"Wi-Fi is not working in Block C"}`.
If AI is offline, the Java backend uses a deterministic fallback classifier.

## Demo flow
1. Register a student or faculty account.
2. For faculty, set `approved=true` in MySQL before login (admin approval workflow).
3. Login and create a complaint.
4. Login as staff/admin to assign and update it.
5. Resolve complaint, add remarks, and submit feedback.
6. View analytics.

## Main API endpoints
- `POST /api/auth/register`
- `POST /api/auth/login`
- `GET /api/auth/me`
- `GET /api/complaints`
- `POST /api/complaints`
- `GET /api/complaints/{id}`
- `PATCH /api/complaints/{id}/status`
- `PATCH /api/complaints/{id}/assign`
- `POST /api/complaints/{id}/feedback`
- `GET /api/admin/analytics`
- `GET /api/admin/complaints/export`
- `GET /api/admin/escalate-overdue`
- `GET /api/meta/departments`
- `GET /api/meta/categories`

## Important security notes
- Change JWT secret and database credentials before deployment.
- Use HTTPS in deployment.
- Add rate limiting, email verification, password reset provider, virus scanning, object storage, and audit retention policies before production use.
- Do not use real Aadhaar or other sensitive identity numbers for this demo.
