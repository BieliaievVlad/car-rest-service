# Car Rest Service
The application is designed to interact with a Postgres database.

## Requirements

- Java 17 (or later version)
- Maven
- PostgreSQL
- Docker
- Docker Compose

## Build
1. Use the following command from the root folder of the project:

	mvn clean package

## Installation
1. Create a PostgreSQL database named `car_service`.

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

### Option 1. 
Run the application, specifying database connection parameters through the command line from the 'target' folder if needed.
For example:

	java -Dspring.datasource.username=postgres -Dspring.datasource.password=postgres -Dspring.flyway.user=postgres -Dspring.flyway.password=postgres -Dspring.config.name=application -Dspring.datasource.url=jdbc:postgresql://localhost:5432/car_service_vb -Dspring.flyway.url=jdbc:postgresql://localhost:5432/car_service_vb -Dserver.port=8088 -jar target/car-rest-service-0.0.1-SNAPSHOT.jar

By default, the application is available at the following address:

	http://localhost:7000
	
### Option 2. 
Run the following command from 'src/main/docker' folder to start the application:

	docker-compose up
	
By default, the application is available at the following address:

	http://localhost:7000
	
To stop the containers when you're done:
	
	docker-compose down
	
## Access Swagger UI
Once the application is running, you can access the Swagger UI by navigating to the following URL in your browser:

- By default:

	http://localhost:7000/swagger-ui/index.html
	
- Or, if using a different port (e.g., 8088):

	http://localhost:8088/swagger-ui/index.html
	
This will open the Swagger documentation where you can explore and test the API endpoints.
	
## Obtaining the Bearer Token

Make a **POST** request to the following URL:

	https://dev-crfs6orqktz444bw.eu.auth0.com/oauth/token

Set the **Content-Type** header:

    Content-Type: application/json

In the body of the request, include the following parameters:

    {
      "client_id": "CnfhRqnszqIpPnV0OnbXLRnyjZyOpfnM",
      "client_secret": "AeGJnmPAbLEJRDhs4TvTSF1GeV_V7HhdhlmTLeZOULsMbcnmfoFQwQIJK8KemOBu",
      "audience": "https://car-service.example.com",
      "grant_type": "client_credentials"
    }

If the request is successful, you will receive a response containing the Bearer Token. Example response:

    {
      "access_token": "YOUR_ACCESS_TOKEN",
      "expires_in": 86400,
      "token_type": "Bearer"
    }

Copy the value from the `access_token` field — this is your Bearer Token.
	
## Endpoints

### 1. Get Filtered Cars
- **URL**: `/api/v1/cars`
- **Method**: `GET`
- **Description**: Retrieves a list of all Cars in the system. Supports pagination and filtering by make, model, category, and year.

#### Request Parameters:
- `makeName` (optional) — Car Make (e.g., "Ford").
- `modelName` (optional) — Car Model (e.g., "Focus").
- `categoryName` (optional) — Car Category (e.g., "Hatchback").
- `year` (optional) — year of production (e.g., 2022).
- `page` (optional, default = 0) — Page number for pagination (starts at 0).
- `size` (optional, default = 10) — Number of items per page.

#### Example Request:
	GET http://localhost:8080/api/v1/cars?makeName=Ford&modelName=Focus&year=2022&page=0&size=5
	
### 2. Get a Car by ID
- **URL**: `/api/v1/cars/{id}`
- **Method**: `GET`
- **Description**: Retrieves information about a Car with the specified ID.

#### Example Request:
	GET http://localhost:8080/api/v1/cars/1

### 3. Create a new Car
- **URL**: `/api/v1/cars`
- **Method**: `POST`
- **Description**: Adds a new Car

#### Example Request:
	POST http://localhost:8080/api/v1/cars
	Content-Type: application/json
	Authorization: Bearer your_token_here

	{
    	"make": "LADA",
    	"model": "KALINA",
    	"category": "Sedan",
    	"year": "2026",
    	"objectId": ""
	}

### 4. Update an existing Car
- **URL**: `/api/v1/cars/{id}`
- **Method**: `PUT`
- **Description**: Updates information for a Car with the specified ID.

#### Example Request:
	PUT http://localhost:8080/api/v1/cars/{1}
	Content-Type: application/json
	Authorization: Bearer your_token_here

	{
    	"make": "LADA",
    	"model": "KALINA",
    	"category": "Sedan",
    	"year": "2026"
	}

### 5. Delete a Car
- **URL**: `/api/v1/cars/{id}`
- **Method**: `DELETE`
- **Description**: Deletes a Car with the specified ID.

#### Example Request:
	DELETE http://localhost:8080/api/v1/cars/1
	Authorization: Bearer your_token_here
	
### 6. Get Filtered Makes
- **URL**: `/api/v1/makes`
- **Method**: `GET`
- **Description**: Retrieves a list of all Makes in the system. Supports pagination and filtering by name.

#### Request Parameters:
- `name` (optional) — Make Name (e.g., "Ford").
- `page` (optional, default = 0) — Page number for pagination (starts at 0).
- `size` (optional, default = 10) — Number of items per page.

#### Example Request:
	GET http://localhost:8080/api/v1/makes?name=Ford&page=0&size=5
	
### 7. Get a Make by ID
- **URL**: `/api/v1/makes/{id}`
- **Method**: `GET`
- **Description**: Retrieves information about a Make with the specified ID.

#### Example Request:
	GET http://localhost:8080/api/v1/makes/1
	
### 8. Create a new Make
- **URL**: `/api/v1/makes`
- **Method**: `POST`
- **Description**: Adds a new Make

#### Example Request:
	POST http://localhost:8080/api/v1/makes
	Content-Type: application/json
	Authorization: Bearer your_token_here

	{
    	"name": "LADA"
	}
	
### 9. Update an existing Make
- **URL**: `/api/v1/makes/{id}`
- **Method**: `PUT`
- **Description**: Updates information for a Make with the specified ID.

#### Example Request:
	PUT http://localhost:8080/api/v1/makes/1
	Content-Type: application/json
	Authorization: Bearer your_token_here

	{
    	"name": "LADA"
	}
	
### 10. Delete a Make
- **URL**: `/api/v1/makes/{id}`
- **Method**: `DELETE`
- **Description**: Deletes a Make with the specified ID.

#### Example Request:
	DELETE http://localhost:8080/api/v1/makes/1
	Authorization: Bearer your_token_here
	
### 11. Get Filtered Models
- **URL**: `/api/v1/models`
- **Method**: `GET`
- **Description**: Retrieves a list of all Models in the system. Supports pagination and filtering by name.

#### Request Parameters:
- `name` (optional) — Model Name (e.g., "Focus").
- `page` (optional, default = 0) — Page number for pagination (starts at 0).
- `size` (optional, default = 10) — Number of items per page.

#### Example Request:
	GET http://localhost:8080/api/v1/models?name=Focus&page=0&size=5
	
### 12. Get a Model by ID
- **URL**: `/api/v1/models/{id}`
- **Method**: `GET`
- **Description**: Retrieves information about a Model with the specified ID.

#### Example Request:
	GET http://localhost:8080/api/v1/models/1
	
### 13. Create a new Model
- **URL**: `/api/v1/models`
- **Method**: `POST`
- **Description**: Adds a new Model

#### Example Request:
	POST http://localhost:8080/api/v1/models
	Content-Type: application/json
	Authorization: Bearer your_token_here

	{
    	"name": "KALINA"
	}
	
### 14. Update an existing Model
- **URL**: `/api/v1/models/{id}`
- **Method**: `PUT`
- **Description**: Updates information for a Model with the specified ID.

#### Example Request:
	PUT http://localhost:8080/api/v1/models/1
	Content-Type: application/json
	Authorization: Bearer your_token_here

	{
    	"name": "KALINA"
	}
	
### 15. Delete a Model
- **URL**: `/api/v1/models/{id}`
- **Method**: `DELETE`
- **Description**: Deletes a Model with the specified ID.

#### Example Request:
	DELETE http://localhost:8080/api/v1/models/1
	Authorization: Bearer your_token_here
	
### 16. Get Filtered Categories
- **URL**: `/api/v1/categories`
- **Method**: `GET`
- **Description**: Retrieves a list of all Categories in the system. Supports pagination and filtering by name.

#### Request Parameters:
- `name` (optional) — Category Name (e.g., "Sedan").
- `page` (optional, default = 0) — Page number for pagination (starts at 0).
- `size` (optional, default = 10) — Number of items per page.

#### Example Request:
	GET http://localhost:8080/api/v1/categories?name=Sedan&page=0&size=5
	
### 17. Get a Category by ID
- **URL**: `/api/v1/categories/{id}`
- **Method**: `GET`
- **Description**: Retrieves information about a Category with the specified ID.

#### Example Request:
	GET http://localhost:8080/api/v1/categories/1
	
### 18. Create a new Category
- **URL**: `/api/v1/categories`
- **Method**: `POST`
- **Description**: Adds a new Category

#### Example Request:
	POST http://localhost:8080/api/v1/categories
	Content-Type: application/json
	Authorization: Bearer your_token_here

	{
    	"name": "Sedan"
	}
	
### 19. Update an existing Category
- **URL**: `/api/v1/categories/{id}`
- **Method**: `PUT`
- **Description**: Updates information for a Category with the specified ID.

#### Example Request:
	PUT http://localhost:8080/api/v1/categories/1
	Content-Type: application/json
	Authorization: Bearer your_token_here

	{
    	"name": "Sedan"
	}
	
### 20. Delete a Category
- **URL**: `/api/v1/categories/{id}`
- **Method**: `DELETE`
- **Description**: Deletes a Category with the specified ID.

#### Example Request:
	DELETE http://localhost:8080/api/v1/categories/1
	Authorization: Bearer your_token_here
	