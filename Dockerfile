# ===============================
# Dockerfile for cart-microservice
# ===============================

# 1. Use a lightweight Java 17 runtime image
FROM eclipse-temurin:17-jdk-alpine

# 2. Set working directory inside the container
WORKDIR /app

# 3. Copy the JAR file built by Maven into the container
COPY target/cart-microservice.jar /app/cart-microservice.jar

# 4. Expose the port your Spring Boot app runs on
EXPOSE 8081

# 5. Run the Spring Boot application
ENTRYPOINT ["java", "-jar", "cart-microservice.jar"]
