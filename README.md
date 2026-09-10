# Secure Storage

A full-stack secure file management system built to demonstrate practical application security — encrypted file storage, robust authentication, and fine-grained access control, built with a Spring Boot backend and a React frontend.

## Overview

Secure Storage lets users upload, store, and manage files with encryption applied at rest, protected behind a layered authentication system. It was built as a portfolio project focused on security engineering — covering encryption, auth flows, and access control end to end rather than treating them as an afterthought.

## Features

- **AES-256 file encryption** — files are encrypted before storage, not just protected by access rules
- **JWT authentication** — stateless, token-based session handling
- **Role-Based Access Control (RBAC)** — different permission levels for different user roles
- **Two-Factor Authentication (2FA)** — adds a second verification step beyond password login
- **OTP-based password recovery** — one-time-password flow for secure account recovery via email

## Tech Stack

**Backend**
- Java 21
- Spring Boot
- Spring Data JPA
- PostgreSQL
- Maven

**Frontend**
- React
- Vite

## Project Structure

```
secure-storage-app/
├── backend/          # Spring Boot API — auth, encryption, file handling
│   └── src/main/resources/
│       ├── application-example.properties   # placeholder config (safe to view)
│       └── application.properties           # real local config (not committed)
├── frontend/         # React + Vite client
└── README.md
```

## Prerequisites

Before you begin, make sure you have installed:

- Java 21 (JDK)
- Maven
- Node.js and npm
- PostgreSQL (running locally, or a connection string to a hosted instance)

## Setup

### 1. Clone the repository

```bash
git clone https://github.com/<your-username>/secure-storage-app.git
cd secure-storage-app
```

### 2. Configure the backend

Copy the example config and fill in your own values:

```bash
cd backend/src/main/resources
cp application-example.properties application.properties
```

Open `application.properties` and replace each placeholder with your real values:

| Placeholder | What it is |
|---|---|
| `${DB_USERNAME}` / `${DB_PASSWORD}` | Your local PostgreSQL credentials |
| `${ADMIN_PASSWORD}` | Password for the built-in admin/security user |
| `${APP_ENCRYPTION_KEY}` | A 32-byte secret key used for AES-256 file encryption |
| `${MAIL_USERNAME}` / `${MAIL_PASSWORD}` | Email account used to send OTP/recovery emails (e.g. a Gmail app password) |

> `application.properties` is git-ignored and never pushed — your real credentials stay local.

Create the PostgreSQL database referenced in `spring.datasource.url`:

```sql
CREATE DATABASE securestorage;
```

### 3. Run the backend

```bash
cd backend
mvn spring-boot:run
```

The API will start on `http://localhost:8080` by default.

### 4. Run the frontend

```bash
cd frontend
npm install
npm run dev
```

The app will be available at `http://localhost:5173` by default (or whatever port Vite reports).

## Environment Variables Reference

If you prefer setting values as actual environment variables instead of editing `application.properties` directly:

```bash
export DB_USERNAME=your_db_username
export DB_PASSWORD=your_db_password
export ADMIN_PASSWORD=your_admin_password
export APP_ENCRYPTION_KEY=your_32_byte_secret_key
export MAIL_USERNAME=your_email@example.com
export MAIL_PASSWORD=your_app_password
```

## Security Notes

- File contents are encrypted with AES-256 before being persisted.
- Passwords and secrets are never hardcoded in tracked files — see `application-example.properties` for the expected configuration shape.
- 2FA and OTP-based recovery reduce reliance on password strength alone.

## License

This project is available for portfolio and educational reference. Feel free to open an issue if you spot something worth improving.
