# Car Rest Service
The application is designed to interact with a Postgres database.

## Requirements

- Java 17 (or later version)
- Maven
- PostgreSQL

## Build
1. Use the following command from the root folder of the project:

	mvn clean package

## Installation
1. Create a PostgreSQL database named 'car_service'.

2. Default settings:

	spring.application.name=Car Service

	spring.jpa.properties.hibernate.default_schema=car_service

	spring.datasource.url=jdbc:postgresql://localhost:5432/car_service 
	spring.datasource.username=postgres 
	spring.datasource.password=123456

	spring.flyway.enabled=true 
	spring.flyway.url=jdbc:postgresql://localhost:5432/car_service 
	spring.flyway.user=postgres 
	spring.flyway.password=123456

## Running the Application
1. Run the application, specifying database connection parameters through the command line from the 'target' folder if needed.
For example:

	java -Dspring.datasource.username=postgres -Dspring.datasource.password=123456 -Dspring.flyway.user=postgres -Dspring.flyway.password=123456 -Dspring.config.name=application -jar car-rest-service-0.0.1-SNAPSHOT.jar

By default, the application is available at the following address:

	http://localhost:8080
	
	