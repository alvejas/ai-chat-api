# AI Chat API

REST + WebSocket backend for the AI Chat application, built with Spring Boot 4 and PostgreSQL.

## Tech Stack

- **Java 21**
- **Spring Boot 4** — Web, Data JPA, Security, Validation, WebSocket
- **PostgreSQL 16** — via Docker
- **JWT** — stateless authentication (jjwt)
- **Lombok** — boilerplate reduction
- **Maven** — build tool

## Prerequisites

- Java 21+
- Maven (or use the included `./mvnw` wrapper)
- Docker & Docker Compose

## Getting Started

### 1. Start the database

```bash
docker-compose up -d
```

This starts a PostgreSQL 16 instance on port **5434** with:
- Database: `aichat_db`
- User: `user`
- Password: `password`

### 2. Run the API

```bash
./mvnw spring-boot:run
```

The server starts on **http://localhost:8080**.

> `spring.jpa.hibernate.ddl-auto=create-drop` is set by default, so the schema is recreated on every restart.

## Project Structure

```
src/main/java/com/aichat/api/
├── auth/           # Registration & login (JWT issuance)
├── channel/        # Channel CRUD
├── message/        # Message persistence & WebSocket relay
├── user/           # User management
└── common/
    ├── boundary/   # Global exception handler
    ├── config/     # WebSocket configuration
    └── security/   # JWT filter, security filter chain
```

## API Reference

All endpoints except `/api/auth/**` and `/ws-chat/**` require a `Bearer` token.

### Auth

| Method | Path | Description |
|--------|------|-------------|
| `POST` | `/api/auth/register` | Register a new user |
| `POST` | `/api/auth/login` | Login and receive a JWT |

### Users

| Method | Path | Description |
|--------|------|-------------|
| `GET` | `/api/users` | List all users |
| `GET` | `/api/users/{username}` | Get user by username |

### Channels

| Method | Path | Description |
|--------|------|-------------|
| `POST` | `/api/channels` | Create a channel |
| `GET` | `/api/channels` | List channels for the authenticated user |
| `GET` | `/api/channels/{name}` | Get channel by name |

**Create channel body:**
```json
{
  "name": "general",
  "description": "optional",
  "creatorUsername": "alice",
  "receiverUsername": "bob"
}
```

### Messages

| Method | Path | Description |
|--------|------|-------------|
| `GET` | `/api/messages/channel/{channelName}` | Get message history for a channel |

### WebSocket

Connect to `ws://localhost:8080/ws-chat` using STOMP over SockJS.

| Destination | Direction | Description |
|-------------|-----------|-------------|
| `/app/chat.send` | Client → Server | Send a message |
| `/topic/channel/{channelName}` | Server → Client | Receive messages for a channel |

## Running Tests

```bash
./mvnw test
```
