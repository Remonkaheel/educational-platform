# Educational Platform — Backend API

A RESTful backend for an online education platform built with **Spring Boot**. It provides
student and instructor accounts, course and category management, resumable video upload and
streaming, and password recovery through email OTP — secured with stateless JWT authentication.

> The application source code lives on the **`master`** branch.

---

## Features

- **Authentication** — registration and login issuing JWT access tokens (stateless sessions).
- **User types** — `STUDENT` and `INSTRUCTOR`, mapped with JPA joined inheritance.
- **Profiles** — retrieve the authenticated user's profile, upload a profile picture, and store
  skills/interests (categories), professional title, and personal links.
- **Password recovery** — email OTP with expiry time and a maximum number of verification attempts.
- **Courses & categories** — create, list, filter by category or instructor, and delete.
- **Video pipeline** — chunked (resumable) upload, storage on disk, and streaming with HTTP range support.
- **API documentation** — interactive Swagger UI.

---

## Tech Stack

| Layer | Technology |
| --- | --- |
| Language | Java 17 |
| Framework | Spring Boot 3.4.0 |
| Security | Spring Security + JWT (jjwt 0.11.5) |
| Persistence | Spring Data JPA / Hibernate |
| Database | MySQL 8 |
| Email | Spring Boot Mail (SMTP) |
| API docs | SpringDoc OpenAPI (Swagger UI) |
| Build tool | Maven (wrapper included) |

---

## Project Structure

```
src/main/java/com/global/hr
├── config            # Security, JWT, Swagger and FTP configuration
│   └── jwt           # JwtAuthFilter, JwtConfig, JwtUtil
├── controllers       # REST controllers (auth, user, course, category, upload, ftp)
├── models            # JPA entities (User, Student, Instructor, Course, Category, Video, Comment, ...)
│   └── DTO           # Request / response DTOs
├── repositories      # Spring Data JPA repositories
├── services          # Business logic
└── mapper            # Entity -> DTO mappers
```

---

## API Endpoints

### Authentication — `/api/auth` (public)

| Method | Endpoint | Description |
| --- | --- | --- |
| POST | `/api/auth/register` | Register a student or instructor (multipart form) |
| POST | `/api/auth/login` | Log in and receive a JWT |
| POST | `/api/auth/forgot-password` | Send a password-reset OTP to an email address |
| POST | `/api/auth/verify-otp` | Verify an OTP |
| POST | `/api/auth/reset-password` | Reset the password after OTP verification |

### User — `/api/user`

| Method | Endpoint | Description |
| --- | --- | --- |
| GET | `/api/user/profile` | Get the authenticated user's profile |

### Courses — `/api/courses`

| Method | Endpoint | Description |
| --- | --- | --- |
| GET | `/api/courses` | List all courses |
| GET | `/api/courses/{id}` | Get a course by id |
| GET | `/api/courses/category/{categoryId}` | List courses in a category |
| GET | `/api/courses/instructor/{instructorId}` | List courses created by an instructor |
| POST | `/api/courses/add` | Create a course |
| DELETE | `/api/courses/{id}` | Delete a course |

### Categories — `/api/categories`

| Method | Endpoint | Description |
| --- | --- | --- |
| GET | `/api/categories` | List all categories |
| GET | `/api/categories/{id}` | Get a category by id |
| POST | `/api/categories/add` | Create a category |
| DELETE | `/api/categories/{id}` | Delete a category |

### Video upload (chunked) — `/api/upload`

| Method | Endpoint | Description |
| --- | --- | --- |
| POST | `/api/upload/initialize` | Start an upload session |
| POST | `/api/upload/chunk` | Upload a single chunk |
| POST | `/api/upload/finalize` | Assemble the chunks into the final file |
| GET | `/api/upload/status/{uploadId}` | Get upload progress |
| GET | `/api/upload/chunks/{uploadId}` | List the chunks already received |

All endpoints except `/api/auth/**` and the Swagger routes require a valid
`Authorization: Bearer <token>` header.

---

## Getting Started

### Prerequisites

- JDK 17 or newer
- MySQL 8
- Maven (or use the bundled wrapper `mvnw` / `mvnw.cmd`)

### 1. Clone the repository

```bash
git clone https://github.com/Remonkaheel/educational-platform.git
cd educational-platform
git checkout master
```

### 2. Create the database

```sql
CREATE DATABASE educational_platform;
```

### 3. Configure the environment

Secrets are **not** stored in the source tree. Provide them through environment variables
(see [Configuration](#configuration)). Any value that is left unset falls back to its local
development default.

### 4. Run the application

```bash
./mvnw spring-boot:run      # Linux / macOS
mvnw.cmd spring-boot:run    # Windows
```

The API starts on `http://localhost:8080`, and Swagger UI is available at
`http://localhost:8080/swagger-ui.html`.

---

## Configuration

All configuration lives in `src/main/resources/application.properties`. Secrets are injected
from environment variables:

| Variable | Description | Default |
| --- | --- | --- |
| `DB_URL` | JDBC URL of the database | `jdbc:mysql://localhost:3306/educational_platform` |
| `DB_USERNAME` | Database user | `root` |
| `DB_PASSWORD` | Database password | *(empty)* |
| `MAIL_USERNAME` | SMTP account used to send OTP emails | *(empty)* |
| `MAIL_PASSWORD` | SMTP app password | *(empty)* |
| `MAIL_FROM` | Sender address for outgoing mail | *(empty)* |
| `JWT_SECRET` | HMAC signing key — use at least 32 random bytes in production | dev placeholder |
| `JWT_EXPIRATION` | Token lifetime in milliseconds | `86400000` (24 h) |
| `CORS_ALLOWED_ORIGINS` | Comma-separated list of allowed origins | `*` |
| `FTP_ENABLED` | Enable the embedded FTP server | `false` |
| `FTP_HOST` | Host advertised to upload clients | `localhost` |
| `FTP_PORT` | FTP server port | `2121` |
| `FTP_USERNAME` | FTP user | *(empty)* |
| `FTP_PASSWORD` | FTP password | *(empty)* |
| `FTP_HOME_DIR` | FTP home directory | `src/main/resources/static/videos` |

---

## Security Notes

The following hardening measures are already in place:

- No credentials or keys are committed to the repository — they are read from environment variables.
- The JWT signing key is never written to the logs.
- The embedded FTP server is **disabled by default** and refuses to start unless credentials are
  explicitly configured.

Reminders for deployment:

- Generate a strong, random `JWT_SECRET` (at least 32 bytes) and rotate it if it is ever exposed.
- Use a dedicated database user instead of `root`, and never leave the password empty.
- Set `CORS_ALLOWED_ORIGINS` to your real frontend origin instead of `*`.
- Store user uploads outside `src/main/resources` (for example, an external directory) so they
  survive rebuilds and deployments.

---

## Author

- GitHub: [@Remonkaheel](https://github.com/Remonkaheel)
