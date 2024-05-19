# Use a base image with Java installed
FROM openjdk:17

# Set the working directory in the container
WORKDIR /app

# Copy the packaged Spring Boot application JAR file into the container
COPY target/GGram.jar app.jar

# Expose the port that the Spring Boot application will run on

# Run the Spring Boot application when the container starts
ENTRYPOINT [ "java", "-jar", "app.jar"]
