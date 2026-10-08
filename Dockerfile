# Build stage
FROM maven:3.9-eclipse-temurin-21 AS builder
WORKDIR /app
COPY backend/samana-backend/ .
RUN mvn clean package -DskipTests

# Runtime stage
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Install curl and download Cloud SQL Proxy
RUN apk add --no-cache curl bash && \
    curl -o cloud-sql-proxy https://dl.google.com/cloudsql/cloud_sql_proxy.linux.amd64 && \
    chmod +x cloud-sql-proxy

COPY --from=builder /app/target/samana-backend-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080

# Set environment variables
ENV PORT=8080
ENV SPRING_PROFILES_ACTIVE=cloud
ENV CLOUD_SQL_INSTANCE=project-c3260d5b-68c2-4c8f-b0d:asia-south1:samana-postgres

CMD ["java", "-jar", "app.jar"]
