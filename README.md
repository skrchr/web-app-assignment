# Web App Assignment - Monorepo

This monorepo contains an Angular frontend and Spring Boot backend, both containerized with Docker Compose.

## Project Structure

```
web-app-assignment/
├── frontend/          # Angular application
├── backend/           # Spring Boot application
├── docker-compose.yml # Docker Compose configuration
└── README.md
```

## Prerequisites

- Docker
- Docker Compose
- Node.js (for local development)
- Java 20 (for local development)
- Maven (for local development)

## Quick Start

### Using Docker Compose (Recommended)

1. **Build and start all services:**

   ```bash
   docker-compose up --build
   ```

2. **Access the applications:**

   - Frontend: http://localhost:4200
   - Backend API: http://localhost:8080
   - H2 Database Console: http://localhost:8080/h2-console

3. **Stop all services:**
   ```bash
   docker-compose down
   ```

### Development Mode

#### Frontend Development

```bash
cd frontend
npm install
npm start
```

Access at: http://localhost:4200

#### Backend Development

```bash
cd backend
mvn spring-boot:run
```

Access at: http://localhost:8080

## Services

### Frontend (Angular)

- **Port:** 4200
- **Framework:** Angular 17
- **Web Server:** Nginx
- **Features:** Routing, CSS styling

### Backend (Spring Boot)

- **Port:** 8080
- **Framework:** Spring Boot 3.2.0
- **Java Version:** 20
- **Database:** H2 (in-memory)
- **Features:** Web, JPA, H2 Console, DevTools

## API Configuration

The frontend is configured to proxy API requests to the backend:

- Frontend API calls to `/api/*` are automatically forwarded to the backend
- Backend runs on port 8080 internally
- Frontend runs on port 4200 externally

## Database

- **Type:** H2 In-Memory Database
- **Console:** Available at http://localhost:8080/h2-console
- **JDBC URL:** `jdbc:h2:mem:testdb`
- **Username:** `sa`
- **Password:** (leave empty)

## Docker Commands

```bash
# Build all services
docker-compose build

# Start services in background
docker-compose up -d

# View logs
docker-compose logs -f

# Stop services
docker-compose down

# Rebuild and start
docker-compose up --build

# Remove volumes and networks
docker-compose down -v --remove-orphans
```

## Development Tips

1. **Hot Reload:** Both applications support hot reload in development mode
2. **Database:** H2 database resets on each restart
3. **CORS:** Backend is configured to allow frontend requests
4. **Logs:** Use `docker-compose logs -f [service-name]` to follow logs
