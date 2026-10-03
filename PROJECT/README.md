# VoteHub — Secure Digital Voting Platform

VoteHub is a college/demo full-stack voting application built with React + Vite, Spring Boot 3.5.5, Java 21 and MySQL 8.

## Included
- Modern responsive VoteHub UI
- Voter and Admin registration
- Demo OTP verification (simulated; no real Aadhaar/UIDAI integration)
- Secure password hashing and protected sessions
- Election browsing and live election room
- One-vote enforcement with database-backed authorization
- Privacy-aware ballot storage (votes do not store voter_id)
- Voter participation history
- Profile and security view
- Admin aggregate dashboard
- MySQL schema with 12 tables and sample elections/candidates

## Database
Run the SQL files in `database/` in this order:
1. `01_create_database.sql`
2. `02_create_tables.sql`
3. `03_sample_data.sql`
4. `04_verify_database.sql`

The sample data creates two demo elections and five candidates. It does not create voter/admin accounts; create those through the website.

## Backend
From `backend/`:
```powershell
mvn clean spring-boot:run
```
Runs on `http://localhost:8080`.

If your MySQL root password is not `root`, set `DB_PASSWORD` or update `backend/src/main/resources/application.properties`.

## Frontend
From `frontend/`:
```powershell
npm install
npm run dev
```
Open `http://localhost:5173`.

## Notes
- OTP shown in the UI is explicitly a demo/simulation code.
- This project is for educational/demo use and is not a production election system.
- The UI provides aggregate results/participation and avoids displaying a voter-to-candidate mapping.
