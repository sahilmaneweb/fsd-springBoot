# Use the official openjdk image from Docker Hub
FROM openjdk:17-jdk-slim

# Set the working directory in the container
WORKDIR /app

# Copy the jar file from the host to the container
COPY target/fsd-0.0.1-SNAPSHOT.jar /app/app.jar

# Expose the port your Spring Boot app runs on (default is 8080)
EXPOSE 8080

# Command to run the Spring Boot app
CMD ["java", "-jar", "app.jar"]
