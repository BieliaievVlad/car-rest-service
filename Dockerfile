FROM openjdk:17-jdk-slim
WORKDIR /app

COPY target/car-rest-service-0.0.1-SNAPSHOT.jar application.jar
COPY src/main/resources/application-docker.properties /app/application-docker.properties
COPY src/main/resources/application-render.properties /app/application-render.properties

ENTRYPOINT ["java", "-jar", "application.jar"]
