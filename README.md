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
- **TMDB API Key** (Required - see setup below)
- Node.js (for local development)
- Java 20 (for local development)
- Maven (for local development)

## Setup

### 1. Get TMDB API Key

### 2. Configure API Key

Create a `local.properties` file in the project root:

```bash
# Copy the example file
cp local.properties.example local.properties
```

Edit `local.properties` and add your API key:

```properties
tmdb.api.key=your_actual_api_key_here
```

**Important:** This file is gitignored and will not be committed.

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

## Technology Stack

### Frontend

- **Framework:** Angular 19
- **UI Library:** PrimeNG 19 (Netflix-style red theme)
- **HTTP Client:** Angular HttpClient with RxJS
- **Routing:** Angular Router
- **Styling:** SCSS with responsive design

### Backend

- **Framework:** Spring Boot 3.2.0
- **Language:** Java 20
- **Build Tool:** Maven
- **Architecture:** Clean Architecture (Boundary-Control-Entity pattern)
- **Testing:** JUnit 5, Integration Tests

### DevOps

- **Containerization:** Docker
- **Orchestration:** Docker Compose
- **Web Server:** Nginx (production)

## Architecture

- **Frontend** → Communicates with backend via REST API
- **Backend** → Proxies requests to TMDB API
- **TMDB API** → Source of movie data

API requests flow: `Frontend (localhost:4200)` → `Backend (localhost:8080)` → `TMDB API`
