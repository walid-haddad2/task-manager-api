[![GitHub Actions](https://github.com/walid-haddad2/task-manager-api/actions/workflows/ci.yml/badge.svg)](https://github.com/walid-haddad2/task-manager-api/actions/workflows/ci.yml)


# Task Manager API

A production-ready RESTful API for task management, built with **Spring Boot 3**, **Spring Security**, and **JWT authentication**.


## Features

- **JWT Authentication** — stateless auth with Bearer tokens
- **Role-Based Access Control** — USER and ADMIN roles
- **Full CRUD for Tasks** — create, read, update, delete with ownership enforcement
- **Input Validation** — request validation with meaningful error messages
- **Swagger UI** — interactive API documentation at `/swagger-ui.html`
- **Dockerized** — runs with a single `docker-compose up`

## Tech Stack

| Layer | Technology |
|-------|-----------|
| Language | Java 21 |
| Framework | Spring Boot 3.2 |
| Security | Spring Security + JWT (jjwt 0.12) |
| Database | MySQL 8 |
| ORM | Spring Data JPA / Hibernate |
| Docs | SpringDoc OpenAPI (Swagger UI) |
| DevOps | Docker, Docker Compose |
| Testing | JUnit 5, Mockito |

## Getting Started

### Prerequisites
- Docker & Docker Compose

### Run with Docker

```bash
git clone https://github.com/walid-haddad/task-manager-api.git
cd task-manager-api
docker-compose up --build
```

The API will be available at `http://localhost:8080`
Swagger UI: `http://localhost:8080/swagger-ui.html`

### Run Locally

```bash
# Start MySQL
docker-compose up mysql -d

# Run the app
./mvnw spring-boot:run
```

## API Endpoints

### Auth
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/auth/register` | Register a new user |
| POST | `/api/auth/login` | Login and get JWT token |

### Tasks *(requires Bearer token)*
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/tasks` | Get all your tasks |
| GET | `/api/tasks?status=TODO` | Filter by status |
| GET | `/api/tasks/{id}` | Get a single task |
| POST | `/api/tasks` | Create a task |
| PUT | `/api/tasks/{id}` | Update a task |
| DELETE | `/api/tasks/{id}` | Delete a task |

## Example Usage

**Register:**
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"username":"walid","email":"walid@example.com","password":"secret123"}'
```

**Create a Task:**
```bash
curl -X POST http://localhost:8080/api/tasks \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"title":"Build the frontend","priority":"HIGH"}'
```

## Project Structure

```
src/main/java/com/waliddev/taskmanager/
├── config/         # Security and OpenAPI configuration
├── controller/     # REST controllers (Auth, Task)
├── dto/            # Request/Response DTOs
├── entity/         # JPA entities (User, Task)
├── exception/      # Custom exceptions + global handler
├── repository/     # Spring Data repositories
├── security/       # JWT filter and utilities
└── service/        # Business logic
```

## Running Tests

```bash
./mvnw test
```

---

*Part of my portfolio — see also [task-manager-ui](https://github.com/walid-haddad/task-manager-ui)*
