# Backend Dockerfile

# Use an official Maven image as a build stage
FROM maven:3.9.6-eclipse-temurin-21-alpine AS build

# Set the working directory
WORKDIR /app

# Copy the project files to the container
COPY . .

# Build the application, skipping tests
RUN mvn clean package -DskipTests

# Use an official JRE image for running the application
FROM eclipse-temurin:21-jre-alpine

# Set the working directory
WORKDIR /app

# Copy the jar file from the build stage
COPY --from=build /app/target/*.jar application.jar

# Expose the port the app runs on
EXPOSE 8080

# Start the application
CMD ["java", "-jar", "application.jar"]
