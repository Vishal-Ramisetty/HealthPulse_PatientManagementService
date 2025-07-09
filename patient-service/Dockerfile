FROM maven:3.9.9-eclipse-temurin-21 AS builder
WORKDIR /app
COPY pom.xml .
# Download dependencies first to leverage Docker cache
RUN mvn dependency:go-offline -B
COPY src ./src
RUN mvn clean package -DskipTests
FROM openjdk:17-slim as RUNNER
WORKDIR /app
COPY --from=build /app/target/patient-service-0.0.1-SNAPSHOT.jar ./patient-service.jar
EXPOSE 4000
ENTRYPOINT ["java", "-jar", "patient-service.jar"]
# Health check to ensure the service is running
HEALTHCHECK --interval=30s --timeout=10s --start-period=5s --retries=3 \
  CMD curl -f http://localhost:4000/actuator/health || exit 1
