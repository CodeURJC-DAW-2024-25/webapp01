# Multi-stage Dockerfile for building and running a Java Spring Boot application with an Angular frontend

# Stage 1: Build the Angular frontend
FROM node:22-slim AS frontend-build

# Set the working directory in the container
WORKDIR /app/frontend

# Copy the package.json and package-lock.json files
COPY ../frontend/package.json ../frontend/package-lock.json ./

# Install the dependencies
RUN npm install -y

# Copy the rest of the frontend files (excluding node_modules)
COPY ../frontend/src ./src
COPY ../frontend/angular.json ../frontend/tsconfig.json ../frontend/tsconfig.app.json ../frontend/tsconfig.spec.json ./

# Build the frontend application
RUN npm run build -- --configuration production --base-href=/new/

# Use the official maven/Java 21 image as the base image
FROM maven:3.9.9-eclipse-temurin-21 AS build

# Set the working directory in the container
WORKDIR /app

# Copy the project files into the working directory
COPY ../backend/pom.xml ./
COPY ../backend/src ./src

# Create the directory for the Angular frontend build
RUN mkdir -p src/main/resources/static/new

# Copy the Angular build from the previous stage
COPY --from=frontend-build /app/frontend/dist/angular/browser/ src/main/resources/static/new

# Build the project and package the application
RUN mvn clean package -DskipTests

# Use the official OpenJDK 21 image as the base image
FROM eclipse-temurin:21-jre

# Set the working directory in the container
WORKDIR /app

# Copy the JAR file from the build image into the working directory
COPY --from=build /app/target/*.jar app.jar

# Expose the port that the application runs on [8443]
EXPOSE 8443

# Entrypoint command to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]