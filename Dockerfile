FROM openjdk:17-jdk-slim AS build
WORKDIR /app
COPY . .
RUN chmod +x mvnw
RUN ./mvnw clean package -DskipTests
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/target/car-rest-service-0.0.1-SNAPSHOT.jar application.jar
COPY src/main/resources/application-docker.properties /app/application-docker.properties
COPY src/main/resources/application-render.properties /app/application-render.properties
ARG PORT=8080
ENV PORT=${PORT}
EXPOSE ${PORT}
ENTRYPOINT ["java", "-jar", "application.jar"]
