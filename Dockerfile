# Build stage
FROM maven:3.9-eclipse-temurin-21 AS builder
WORKDIR /app
COPY backend/samana-backend/ .
RUN mvn clean package -DskipTests

# Runtime stage
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

COPY --from=builder /app/target/samana-backend-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080

ENV PORT=8080
ENV SPRING_PROFILES_ACTIVE=cloud

CMD ["java", "-jar", "app.jar"]
