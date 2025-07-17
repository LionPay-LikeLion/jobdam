# Use an OpenJDK 21 base image
FROM eclipse-temurin:21-jdk-alpine

# Set timezone and Java options
ENV TZ=Asia/Seoul
ENV JAVA_OPTS=""

# Create app directory
WORKDIR /app

# Copy the built JAR from Maven
COPY target/jobdam-0.0.1-SNAPSHOT.jar app.jar

# Expose Spring Boot port
EXPOSE 8080

# Run the application
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]