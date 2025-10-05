FROM eclipse-temurin:17-jdk-alpine AS build

WORKDIR /app
COPY pom.xml .
COPY src ./src

# Install Maven
RUN apk add --no-cache maven

# Build the application
RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jre-alpine

RUN apk --no-cache add curl

WORKDIR /app

# Copy the built JAR from the build stage
COPY --from=build /app/target/dashboard-api-*.jar app.jar

# Create a non-root user
RUN addgroup -g 1001 -S dashboard && \
    adduser -S dashboard -u 1001 -G dashboard

USER dashboard

EXPOSE 8080

HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
  CMD curl -f http://localhost:8080/api/v1/health || exit 1

ENTRYPOINT ["java", "-jar", "app.jar"]