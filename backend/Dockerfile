# Build stage
FROM openjdk:20-jdk-slim AS build
WORKDIR /app

# Install Maven
RUN apt-get update && apt-get install -y maven && rm -rf /var/lib/apt/lists/*

COPY backend/pom.xml .
COPY backend/src ./src
COPY local.properties ./local.properties
RUN mvn clean package

# Production stage
FROM openjdk:20-jdk-slim
WORKDIR /app
COPY --from=build /app/target/tmdb-*.war app.war
COPY --from=build /app/local.properties ./local.properties
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.war"]
