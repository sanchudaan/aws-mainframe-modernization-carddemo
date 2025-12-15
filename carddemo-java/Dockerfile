# CardDemo Java 21 Application Dockerfile
# Migrated from IBM Enterprise COBOL 6.3

FROM eclipse-temurin:21-jdk-alpine AS builder

WORKDIR /app

# Copy Gradle wrapper and build files
COPY gradle/ gradle/
COPY gradlew .
COPY build.gradle.kts .
COPY settings.gradle.kts .

# Copy module build files
COPY carddemo-common/build.gradle.kts carddemo-common/
COPY carddemo-core/build.gradle.kts carddemo-core/
COPY carddemo-api/build.gradle.kts carddemo-api/
COPY carddemo-batch/build.gradle.kts carddemo-batch/

# Copy source code
COPY carddemo-common/src carddemo-common/src
COPY carddemo-core/src carddemo-core/src
COPY carddemo-api/src carddemo-api/src
COPY carddemo-batch/src carddemo-batch/src

# Build the application
RUN chmod +x gradlew && ./gradlew :carddemo-api:bootJar --no-daemon

# Runtime stage
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Create non-root user for security
RUN addgroup -S carddemo && adduser -S carddemo -G carddemo

# Copy the built JAR
COPY --from=builder /app/carddemo-api/build/libs/*.jar app.jar

# Set ownership
RUN chown -R carddemo:carddemo /app

USER carddemo

# Expose port
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=30s --retries=3 \
    CMD wget --no-verbose --tries=1 --spider http://localhost:8080/actuator/health || exit 1

# Run the application with virtual threads enabled
ENTRYPOINT ["java", "-XX:+UseZGC", "-XX:+ZGenerational", "-Djava.security.egd=file:/dev/./urandom", "-jar", "app.jar"]
