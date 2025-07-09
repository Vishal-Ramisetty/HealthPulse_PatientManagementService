FROM maven:3.8.6-openjdk-17-slim AS builder
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline -B
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: runtime
FROM openjdk:17-slim AS runner
WORKDIR /app
COPY --from=build /app/target/patient-service-0.0.1-SNAPSHOT.jar \
     ./patient-service.jar
EXPOSE 4000
ENTRYPOINT ["java", "-jar", "patient-service.jar"]