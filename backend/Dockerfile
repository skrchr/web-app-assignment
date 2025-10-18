# Build stage
FROM openjdk:20-jdk-slim AS build
WORKDIR /app

# Install Maven
RUN apt-get update && apt-get install -y maven && rm -rf /var/lib/apt/lists/*

COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Production stage
FROM openjdk:20-jdk-slim
WORKDIR /app
COPY --from=build /app/target/*.war app.war
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.war"]
