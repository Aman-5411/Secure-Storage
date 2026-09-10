# 🔐 Secure Storage

A full-stack secure file management system built with **Spring Boot + React** that focuses on practical application security, encrypted file storage, authentication, authorization, and secure account recovery.

The application allows users to register, authenticate, upload and manage files, while administrators can manage users and uploaded files.

---

## 🚀 Live Application

* **Frontend:** Deployed on Vercel
* **Backend:** Deployed on Render
* **Database:** PostgreSQL hosted on Render

> https://secure-storage-six.vercel.app/

---

## ✨ Features

### 🔐 Authentication & Security

* JWT-based authentication
* Role-Based Access Control (RBAC)
* User and Admin roles
* Protected frontend routes
* Protected backend REST APIs
* Two-Factor Authentication (2FA)
* OTP-based password recovery
* Secure password handling

### 📁 Secure File Management

* Upload files
* Download files
* Delete files
* User-specific file access
* AES-256 encryption for stored files

### 👑 Admin Dashboard

Administrators can:

* View registered users
* View user roles
* Promote users to administrators
* Delete users
* View uploaded files
* Delete uploaded files

### 🌐 Full-Stack Architecture

```text
React + Vite
     │
     │ REST API / Axios
     ▼
Spring Boot
     │
     ├── Spring Security + JWT
     ├── Authentication / Authorization
     ├── File Encryption
     ├── OTP / 2FA
     │
     ▼
PostgreSQL
```

---

# 🛠️ Tech Stack

## Backend

| Technology             | Purpose                        |
| ---------------------- | ------------------------------ |
| Java 21                | Programming language           |
| Spring Boot 3.5.5      | Backend framework              |
| Spring Security        | Authentication & authorization |
| Spring Data JPA        | Database access                |
| Hibernate              | ORM                            |
| PostgreSQL             | Relational database            |
| Maven                  | Dependency management/build    |
| JWT                    | Stateless authentication       |
| Java Cryptography APIs | File encryption                |

## Frontend

| Technology   | Purpose                |
| ------------ | ---------------------- |
| React 19     | UI                     |
| Vite         | Frontend build tool    |
| Axios        | HTTP/API communication |
| React Router | Client-side routing    |
| JavaScript   | Application logic      |

## Deployment

| Service           | Purpose             |
| ----------------- | ------------------- |
| Vercel            | React frontend      |
| Render            | Spring Boot backend |
| Render PostgreSQL | Production database |

---

# 📂 Project Structure

```text
Secure-Storage/
│
├── Backend/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/
│   │   │   │   └── com/
│   │   │   │       └── securestorage/
│   │   │   └── resources/
│   │   │       └── application.properties
│   │   │
│   │   └── test/
│   │
│   ├── Dockerfile
│   ├── pom.xml
│   └── ...
│
├── Frontend/
│   ├── src/
│   ├── public/
│   ├── package.json
│   ├── vite.config.js
│   └── ...
│
├── .gitignore
└── README.md
```

---

# 📋 Prerequisites

Before running the project locally, install:

### Backend

* Java JDK 21
* Maven 3.9+
* PostgreSQL

### Frontend

* Node.js
* npm

Verify your installations:

```bash
java -version
mvn -version
node -v
npm -v
psql --version
```

---

# 💻 Running Locally

## 1. Clone the Repository

```bash
git clone https://github.com/Aman-5411/Secure-Storage.git
```

Move into the project:

```bash
cd Secure-Storage
```

---

# 🗄️ 2. Configure PostgreSQL

Make sure PostgreSQL is installed and running.

Create a database:

```sql
CREATE DATABASE securestorage;
```

You can create the database using PostgreSQL's `psql` shell or a database management tool such as pgAdmin.

---

# ⚙️ 3. Configure the Backend

Navigate to:

```text
Backend/src/main/resources/
```

Create:

```text
application.properties
```

Do **not** commit production credentials or secrets to GitHub.

A local configuration can look like:

```properties
spring.application.name=secure-storage

server.port=8080

# PostgreSQL
spring.datasource.url=jdbc:postgresql://localhost:5432/securestorage
spring.datasource.username=YOUR_POSTGRES_USERNAME
spring.datasource.password=YOUR_POSTGRES_PASSWORD
spring.datasource.driver-class-name=org.postgresql.Driver

# JPA / Hibernate
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=false
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
spring.jpa.properties.hibernate.jdbc.lob.non_contextual_creation=true

# File encryption
app.encryption.secret-key=YOUR_32_BYTE_ENCRYPTION_SECRET

# JWT
app.jwt.secret=YOUR_JWT_SECRET

# Email
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=YOUR_EMAIL
spring.mail.password=YOUR_GMAIL_APP_PASSWORD
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true

# Frontend
app.frontend.url=http://localhost:5173
```

### ⚠️ Important

Never commit:

* Database passwords
* JWT secrets
* Encryption keys
* Gmail passwords
* Gmail App Passwords
* API keys

Your real configuration should remain local or be supplied through environment variables in production.

---

# 🔑 4. Generate Your Secrets

The application requires separate secrets for:

### JWT

Used to sign authentication tokens.

```text
app.jwt.secret
```

### File Encryption

Used for AES-256 file encryption.

```text
app.encryption.secret-key
```

The encryption key must remain stable.

> **Important:** If files have already been encrypted using an encryption key, changing that key can prevent the application from decrypting those files.

---

# 📧 5. Gmail Configuration

If you want OTP/password-recovery emails to work locally, configure Gmail SMTP.

Use a **Gmail App Password**, not your normal Gmail password.

Configure:

```properties
spring.mail.username=YOUR_EMAIL
spring.mail.password=YOUR_GMAIL_APP_PASSWORD
```

You may need to enable 2-Step Verification on the Google account before creating an App Password.

---

# ▶️ 6. Run the Backend

Open a terminal:

```bash
cd Backend
```

Run:

```bash
mvn spring-boot:run
```

Or on Windows, if the Maven wrapper is available:

```powershell
.\mvnw.cmd spring-boot:run
```

The backend should start at:

```text
http://localhost:8080
```

---

# 🎨 7. Run the Frontend

Open a **second terminal**.

From the project root:

```bash
cd Frontend
```

Install dependencies:

```bash
npm install
```

Start the development server:

```bash
npm run dev
```

Vite will normally start the frontend at:

```text
http://localhost:5173
```

Open the URL shown in your terminal.

---

# 🔗 8. Frontend API Configuration

The frontend uses an environment variable to determine where the backend API is hosted.

Create:

```text
Frontend/.env
```

For local development:

```env
VITE_API_URL=http://localhost:8080
```

The frontend will then communicate with:

```text
http://localhost:8080/api
```

### Important

Do not commit `.env` files containing environment-specific configuration or secrets.

---

# 👤 User Registration

Normal registration creates a regular user account.

The expected role is:

```text
ROLE_USER
```

After registration, users can log in and access their protected resources.

---

# 👑 Admin Access

The application contains a protected Admin Dashboard.

Frontend route:

```text
/admin
```

Only users with:

```text
ROLE_ADMIN
```

can access the dashboard.

The backend also protects admin APIs using Spring Security, so changing the frontend route or local storage values alone does **not** grant administrator privileges.

### Creating the First Admin

For a development or production database, an administrator can be promoted directly in PostgreSQL.

Example:

```sql
UPDATE users
SET role = 'ROLE_ADMIN'
WHERE id = YOUR_USER_ID;
```

Verify:

```sql
SELECT id, name, email, role
FROM users
WHERE id = YOUR_USER_ID;
```

After changing the role:

1. Log out.
2. Log in again.
3. A new JWT will be generated containing the updated role.
4. Navigate to:

```text
/admin
```

---

# 🐳 Running the Backend with Docker

The backend includes a Dockerfile.

From the backend directory:

```bash
cd Backend
```

Build the image:

```bash
docker build -t secure-storage-backend .
```

Run it:

```bash
docker run -p 8080:8080 secure-storage-backend
```

For a real deployment, database credentials and application secrets should be provided through environment variables rather than being placed inside the image.

---

# 🧪 Building the Backend

To create the production JAR:

```bash
cd Backend
mvn clean package -DskipTests
```

The generated JAR will be placed inside:

```text
Backend/target/
```

You can then run it with:

```bash
java -jar target/*.jar
```

---

# ☁️ Deployment

The production architecture uses:

```text
                    Internet
                       │
                       ▼
                ┌─────────────┐
                │   Vercel    │
                │   React     │
                └──────┬──────┘
                       │
                    HTTPS
                       │
                       ▼
                ┌─────────────┐
                │   Render    │
                │ Spring Boot │
                └──────┬──────┘
                       │
                       ▼
                ┌─────────────┐
                │   Render    │
                │ PostgreSQL  │
                └─────────────┘
```

---

# 🚀 Deploy Backend to Render

## 1. Create PostgreSQL

Create a PostgreSQL database on Render.

Create/configure:

```text
Database Name
Username
Password
Host
Port
```

Prefer the internal database connection when your backend and database are running in the same Render region.

---

## 2. Create a Render Web Service

Connect the GitHub repository:

```text
https://github.com/Aman-5411/Secure-Storage
```

Configure the service:

```text
Branch: main
Root Directory: Backend
Runtime: Docker
```

Render will use:

```text
Backend/Dockerfile
```

to build the Spring Boot application.

---

# 🔐 Render Environment Variables

Configure the following environment variables in the Render backend service:

```text
DB_HOST
DB_PORT
DB_NAME
DB_USERNAME
DB_PASSWORD

APP_ENCRYPTION_SECRET
JWT_SECRET

MAIL_HOST
MAIL_PORT
MAIL_USERNAME
MAIL_PASSWORD

FRONTEND_URL
```

Example structure:

```text
DB_HOST=<render-postgres-host>
DB_PORT=5432
DB_NAME=securestorage
DB_USERNAME=<database-user>
DB_PASSWORD=<database-password>

APP_ENCRYPTION_SECRET=<your-encryption-secret>
JWT_SECRET=<your-jwt-secret>

MAIL_HOST=smtp.gmail.com
MAIL_PORT=587
MAIL_USERNAME=<your-email>
MAIL_PASSWORD=<your-gmail-app-password>

FRONTEND_URL=https://your-frontend.vercel.app
```

**Never put the actual values in this README.**

The backend configuration maps these environment variables into Spring Boot properties.

---

# 🌐 Deploy Frontend to Vercel

Import the GitHub repository into Vercel.

Configure the project to use the `Frontend` directory as the frontend project root.

Install dependencies:

```bash
npm install
```

Build command:

```bash
npm run build
```

Output directory:

```text
dist
```

---

# 🔗 Vercel Environment Variable

Add:

```text
VITE_API_URL
```

with the value:

```text
https://YOUR-RENDER-BACKEND.onrender.com
```

For example:

```text
VITE_API_URL=https://secure-storage-api.onrender.com
```

The frontend will then make requests to:

```text
https://YOUR-RENDER-BACKEND.onrender.com/api
```

---

# 🔄 CORS Configuration

The backend needs to allow requests from the deployed frontend.

Set:

```text
FRONTEND_URL=https://YOUR-VERCEL-APP.vercel.app
```

in Render.

For local development:

```text
FRONTEND_URL=http://localhost:5173
```

---

# 🔒 Security Considerations

This project is intended as a security-focused portfolio/educational project.

Important security considerations:

* Never commit secrets to GitHub.
* Use environment variables for production credentials.
* Keep JWT secrets private.
* Keep the AES encryption key private.
* Do not expose database credentials.
* Use HTTPS in production.
* Use Gmail App Passwords rather than regular Gmail passwords.
* Do not change the encryption key after encrypted files have been stored unless you have a proper key-rotation/migration strategy.
* Keep administrator accounts protected with strong credentials and 2FA.

---

# 🧩 Troubleshooting

## Backend doesn't start

Check:

```bash
java -version
mvn -version
```

Then verify your PostgreSQL configuration.

Make sure the database exists:

```sql
SELECT current_database();
```

---

## Database connection error

Check:

```properties
spring.datasource.url
spring.datasource.username
spring.datasource.password
```

For local PostgreSQL:

```text
jdbc:postgresql://localhost:5432/securestorage
```

---

## Frontend cannot connect to backend

Check:

```text
Frontend/.env
```

Make sure:

```env
VITE_API_URL=http://localhost:8080
```

Then restart Vite:

```bash
npm run dev
```

Environment variables are loaded when Vite starts.

---

## Admin Dashboard doesn't appear

Verify that the database contains:

```text
ROLE_ADMIN
```

Run:

```sql
SELECT id, name, email, role
FROM users;
```

Then log out and log in again to obtain a new JWT.

---

## CORS error in production

Verify the Render environment variable:

```text
FRONTEND_URL
```

matches the exact Vercel URL.

For example:

```text
https://your-app.vercel.app
```

Do not add an unnecessary trailing `/`.

---

# 📌 API Overview

The backend exposes REST APIs for:

```text
/api/auth/**
```

Authentication and account-related operations.

```text
/api/users/**
```

User-specific operations.

```text
/api/admin/**
```

Administrator operations.

All protected endpoints require authentication through the JWT-based security layer.

---

# 🔮 Future Improvements

Possible future improvements include:

* Refresh-token based authentication
* Token rotation
* Improved encryption mode such as AES-GCM
* Secure key management / KMS integration
* File size limits
* Virus/malware scanning
* Rate limiting
* Audit logging
* Account lockout after repeated failed login attempts
* Email provider API instead of SMTP
* Automated CI/CD pipeline
* Automated integration tests
* Cloud object storage such as S3

---

# 🎯 Purpose of the Project

Secure Storage was developed as a portfolio project to demonstrate practical knowledge of:

* Java
* Spring Boot
* Spring Security
* JWT
* REST APIs
* PostgreSQL
* JPA/Hibernate
* React
* Vite
* Authentication
* Authorization
* RBAC
* 2FA
* OTP-based account recovery
* File encryption
* Docker
* Cloud deployment
* Environment-based configuration

The goal is not only to build a functional file-storage application, but to demonstrate how security considerations can be integrated throughout a full-stack application.

---

# 📄 License

This project is available for portfolio and educational purposes.

Feel free to explore the code, open issues, or suggest improvements.
