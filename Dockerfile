# Stage 1: Build the application jar file
FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /app

# Cache dependencies
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy source code and build jar package
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Create execution image
FROM openjdk:17-jdk-alpine
WORKDIR /app

# Copy built jar from previous stage
COPY --from=build /app/target/*.jar app.jar

# Expose port
EXPOSE 8080

# Execute container
ENTRYPOINT ["java", "-jar", "app.jar"]
