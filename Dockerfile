# Build stage
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app

# Copy the parent pom and module poms first to leverage Docker cache for dependencies
COPY pom.xml .
COPY steganograph-core/pom.xml steganograph-core/
COPY steganograph-desktop/pom.xml steganograph-desktop/
COPY steganograph-web/pom.xml steganograph-web/

# Download dependencies (only for steganograph-web and its dependencies like steganograph-core)
RUN mvn dependency:go-offline -B -pl steganograph-web -am

# Copy the source code for the required modules
COPY steganograph-core/src steganograph-core/src
COPY steganograph-web/src steganograph-web/src

# Build the steganograph-web module
RUN mvn clean package -DskipTests -pl steganograph-web -am

# Run stage
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app

# Copy the built jar from the build stage
COPY --from=build /app/steganograph-web/target/steganograph-web-*.jar app.jar

# Expose the port (configured in application.properties)
EXPOSE 9222

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
