# SecureVault

## Project Overview

SecureVault is a secure credential management backend that allows users to store, organize, and manage sensitive credentials safely. It is built with a strong focus on authentication, encryption, and production-readiness using Spring Boot.

## Key Features

- User registration and login
- JWT authentication with access & refresh tokens
- Spring Security
- BCrypt password hashing
- AES-256-GCM encryption for sensitive credentials
- Credential CRUD APIs
- Category management with nested categories
- Role-Based Access Control (USER/ADMIN)
- Input validation
- Rate limiting
- CORS and security headers
- Search, pagination, and sorting
- Audit logging
- Swagger/OpenAPI documentation
- Actuator health check
- Environment variable configuration
- Graceful shutdown
- Dockerized application

## Tech Stack

- **Java 21**
- **Spring Boot 4**
- **Spring Security**
- **Spring Data JPA / Hibernate**
- **PostgreSQL**
- **JWT**
- **BCrypt**
- **AES-256-GCM**
- **Maven**
- **Docker**
- **Swagger / OpenAPI**

## API Modules

| Module | Endpoint | Description |
|---|---|---|
| Auth | `POST /api/auth/register` | Register a new user |
| Auth | `POST /api/auth/login` | Login and receive tokens |
| Auth | `POST /api/auth/refresh` | Refresh access token |
| Credentials | `GET /api/credentials` | List credentials |
| Credentials | `POST /api/credentials` | Create credential |
| Credentials | `PUT /api/credentials/{id}` | Update credential |
| Credentials | `DELETE /api/credentials/{id}` | Delete credential |
| Categories | `GET /api/categories` | List categories |
| Categories | `POST /api/categories` | Create category |
| Categories | `PUT /api/categories/{id}` | Update category |
| Categories | `DELETE /api/categories/{id}` | Delete category |
| Admin | `/api/admin/**` | Admin-only endpoints |

## Environment Variables

| Variable | Description |
|---|---|
| `DB_URL` | PostgreSQL connection URL |
| `DB_USERNAME` | Database username |
| `DB_PASSWORD` | Database password |
| `JWT_SECRET` | Secret key for JWT |
| `ENCRYPTION_KEY` | AES-256-GCM encryption key |

## How to Run

```bash
git clone <repo-url>
cd securevault

# Set environment variables (create a .env file or export directly)
export DB_URL=jdbc:postgresql://localhost:5432/securevault
export DB_USERNAME=your_db_username
export DB_PASSWORD=your_db_password
export JWT_SECRET=your_jwt_secret
export ENCRYPTION_KEY=your_encryption_key

# Build the project
mvn clean install

# Run the application
mvn spring-boot:run
```

### Run with Docker

```bash
docker build -t securevault .
docker run -p 8080:8080 --env-file .env securevault
```

The application will start on `http://localhost:8080`

## API Documentation

Interactive API documentation is available via Swagger UI:

- **Swagger UI:** [https://securevault-backend-qdvg.onrender.com/swagger-ui/index.html](https://securevault-backend-qdvg.onrender.com/swagger-ui/index.html)
- **Base URL:** [https://securevault-backend-qdvg.onrender.com](https://securevault-backend-qdvg.onrender.com)

## Live Demo

The backend is deployed and hosted at:

🔗 **https://securevault-backend-qdvg.onrender.com**