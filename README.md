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

   Alternatively run the bash script

   ```bash
   ./start.sh
   ```

2. **Access the applications:**

   - Frontend: http://localhost:4200
   - Backend API: http://localhost:8080

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

### Backend (Spring Boot)

- **Port:** 8080
- **Framework:** Spring Boot 3.2.0
- **Java Version:** 20

## API Configuration

The frontend is configured to proxy API requests to the backend:

- Frontend API calls to `/api/*` are automatically forwarded to the backend
- Backend runs on port 8080 internally
- Frontend runs on port 4200 externally
