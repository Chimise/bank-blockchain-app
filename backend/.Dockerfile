# Use the official Maven image as a base
FROM maven:3.8.4-openjdk-17-slim AS build

# Set the working directory in the container
WORKDIR /app

# Copy the Maven project file to the container
COPY pom.xml .

# Download dependencies
RUN mvn dependency:go-offline -B

# Copy the rest of the application code to the container
COPY src ./src

# Build the Spring Boot application
RUN mvn package -DskipTests

# Use the official OpenJDK image as a base to run the Spring Boot app
FROM openjdk:17-slim

# Set the working directory in the container
WORKDIR /app

# Copy the built JAR file from the build stage to the container
COPY --from=build /app/target/*.jar app.jar

# Expose port 8080 for Spring Boot server
EXPOSE 8080

# Command to run the Spring Boot application
CMD ["java", "-jar", "app.jar"]
