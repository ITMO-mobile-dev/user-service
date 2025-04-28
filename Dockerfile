FROM openjdk:17-jdk-slim

WORKDIR /app

COPY gradlew .
COPY gradle gradle

RUN chmod +x ./gradlew

COPY build.gradle.kts .
COPY settings.gradle.kts .

# Resolve dependencies
RUN ./gradlew dependencies --stacktrace || ./gradlew compileKotlin --stacktrace

COPY src src

# Build the application
RUN ./gradlew assemble

# Find the built JAR file
RUN ls build/libs
RUN find build/libs -name "*.jar" -print

# Extract JAR name for ENTRYPOINT
RUN APP_FILE=$(find build/libs -name "*.jar" -print -quit) && \
    echo "Found JAR file: $APP_FILE" && \
    APP_FILE_NAME=$(basename "$APP_FILE") && \
    echo "JAR file name: $APP_FILE_NAME" && \
    export APP_FILE_NAME="$APP_FILE_NAME"

ENV APP_FILE_NAME=user-service.jar

RUN echo "App file name: $APP_FILE_NAME"
# Command to run the application
ENTRYPOINT ["java", "-jar", "build/libs/${APP_FILE_NAME}"]

EXPOSE 8081
