\# SecureVault



\## Project Overview

SecureVault is a secure credential management backend that allows users to store, organize, and manage sensitive credentials safely. It is built with a strong focus on authentication, encryption, and production-readiness, using Spring Boot and modern security practices.



\## Key Features

\- User registration and login

\- JWT authentication with access token + refresh token

\- Spring Security integration

\- BCrypt password hashing

\- AES-256-GCM encryption for sensitive credentials

\- Credential CRUD APIs

\- Category management with nested categories

\- Role-Based Access Control (USER/ADMIN)

\- Input validation

\- Rate limiting

\- CORS and security headers

\- Search, pagination, and sorting

\- Audit logging

\- Swagger/OpenAPI documentation

\- Actuator health check

\- Environment variable based configuration

\- Graceful shutdown

\- Dockerized Spring Boot application



\## Tech Stack

\- \*\*Language:\*\* Java 21

\- \*\*Framework:\*\* Spring Boot 4, Spring Security, Spring Data JPA / Hibernate

\- \*\*Database:\*\* PostgreSQL

\- \*\*Security:\*\* JWT, BCrypt, AES-256-GCM

\- \*\*Build Tool:\*\* Maven

\- \*\*Containerization:\*\* Docker

\- \*\*Documentation:\*\* Swagger / OpenAPI



\## API Modules / Important Endpoints



| Module | Endpoint | Description |

|--------|----------|--------------|

| Auth | `POST /api/auth/register` | Register a new user |

| Auth | `POST /api/auth/login` | Login and receive access + refresh tokens |

| Auth | `POST /api/auth/refresh` | Refresh access token |

| Credentials | `GET /api/credentials` | List credentials (search, pagination, sorting) |

| Credentials | `POST /api/credentials` | Create a new credential |

| Credentials | `PUT /api/credentials/{id}` | Update a credential |

| Credentials | `DELETE /api/credentials/{id}` | Delete a credential |

| Categories | `GET /api/categories` | List categories (supports nesting) |

| Categories | `POST /api/categories` | Create a category |

| Categories | `PUT /api/categories/{id}` | Update a category |

| Categories | `DELETE /api/categories/{id}` | Delete a category |

| Admin | `GET /api/admin/\*\*` | Admin-only endpoints (RBAC protected) |



\## Environment Variables



| Variable | Description |

|----------|-------------|

| `DB\_URL` | PostgreSQL connection URL |

| `DB\_USERNAME` | Database username |

| `DB\_PASSWORD` | Database password |

| `JWT\_SECRET` | Secret key for signing JWT tokens |

| `ENCRYPTION\_KEY` | AES-256-GCM encryption key |





\## How to Run



```bash

git clone <repo-url>

cd securevault

```



Set environment variables (or use a `.env` file), then run:



```bash

.\\mvnw.cmd spring-boot:run

```



\## Docker



```bash

docker build -t securevault .

docker run -d --name securevault-container -p 8080:8080 --env-file .env securevault

```



\## Swagger \& Health Check



\- \*\*Swagger UI:\*\* `http://localhost:8080/swagger-ui/index.html`

\- \*\*Actuator Health:\*\* `http://localhost:8080/actuator/health`





\## Author

\*\*Swastika\*\*

B.Tech IT, KNIT Sultanpur



