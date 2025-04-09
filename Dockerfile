FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/car-rest-service-0.0.1-SNAPSHOT.jar application.jar
COPY src/main/resources/application-docker.properties /app/application.properties
ENTRYPOINT ["java", "-Dspring.profiles.active=docker", "-jar", "application.jar"]

#ENTRYPOINT ["java", 
#			  "-Dspring.datasource.username=postgres", 
#			  "-Dspring.datasource.password=postgres", 
#			  "-Dspring.flyway.user=postgres",
#			  "-Dspring.flyway.password=postgres", 
#			  "-Dspring.config.name=application", 
#			  "-Dspring.datasource.url=jdbc:postgresql://localhost:5432/car_service_vb", 
#			  "-Dspring.flyway.url=jdbc:postgresql://localhost:5432/car_service_vb", 
#			  "-Dserver.port=8088",
#			  "-jar", 
#			  "application.jar"]